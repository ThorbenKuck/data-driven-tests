package com.github.thorbenkuck.ddt.api;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.TestCaseFactoryMetaData;
import com.github.thorbenkuck.ddt.api.annotations.marker.ConfigureTestData;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;
import com.github.thorbenkuck.ddt.api.domain.classpath.PathBasedTestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.empty.EmptyTestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.factory.FactoryBasedTestCaseContent;
import com.github.thorbenkuck.ddt.api.registry.PluginDiscoverer;
import com.github.thorbenkuck.ddt.api.registry.TestContentFactoryRegistry;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.properties.DdtProperties;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SuiteType {
    BLANK {
        @Override
        public List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod, EngineCore engineCore) {
            if (testMethod.getParameterCount() != 0) {
                return Collections.emptyList();
            }

            return Collections.singletonList(new EmptyTestCaseContent(testMethod));
        }
    }, CLASSPATH_RESOURCE {
        @Override
        public List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod, EngineCore engineCore) {
            if (annotation.suite().trim().isEmpty()) {
                throw new IllegalStateException("For the SuiteLocation CLASSPATH_RESOURCE, you'll have to provide a present folder in the \"suite\" field");
            }

            String suiteFolderInClassPath = checked(DdtProperties.suitsRoot()) + annotation.suite();
            URL rootFolderResource = getClass().getClassLoader().getResource(suiteFolderInClassPath);
            if (rootFolderResource != null) {
                try {
                    return Files.list(Paths.get(rootFolderResource.toURI()))
                            .filter(path -> !path.getFileName().toString().contains("." + annotation.expectedFileFlag()))
                            .map(path -> new PathBasedTestCaseContent(path, annotation))
                            .collect(Collectors.toList());
                } catch (URISyntaxException | IOException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                throw new IllegalStateException("Suite folder " + suiteFolderInClassPath + " does not exist in the classpath");
            }
        }
    }, FACTORY {
        @Override
        public List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod, EngineCore engineCore) {
            final TestContentFactoryRegistry testContentFactoryRegistry = PluginDiscoverer.loadTestContentFactories(engineCore);

            // Prioritize annotation based configuration
            OrderedSet<TestCaseFactory<?, ?>> testCaseFactories = TestCaseFactoryMetaData.of(testClass, testMethod)
                    .getFactories()
                    .stream()
                    .map(engineCore::createInstanceOf)
                    .collect(OrderedSet.collector());

            // Only use plugin enabled instances, when not already defined via annotation
            testContentFactoryRegistry.findAllBy(annotation)
                    .stream()
                    .filter(factory -> testCaseFactories.stream().noneMatch(it -> it.getClass().equals(factory.getClass())))
                    .forEach(testCaseFactories::add);

            if (testCaseFactories.isEmpty()) {
                throw new IllegalStateException("No TestCaseContentFactories registered for the suite " + annotation.suite() + ", defined in " + testClass + "." + testMethod.getName());
            }

            List<TestCaseContent> result = testCaseFactories.stream()
                    .flatMap(factory -> {
                        Class<? extends TestCaseFactory> factoryType = factory.getClass();
                        return factory.produceStream()
                                .map(entry -> new FactoryBasedTestCaseContent(factoryType, entry, annotation));
                    })
                    .collect(Collectors.toList());

            if (result.isEmpty()) {
                throw new IllegalStateException("No TestContentFactory for the suite " + annotation.suite() + " produced any scenarios!");
            }
            return result;
        }
    }, CONFIGURATION_ON_TEST_CLASS {
        @Override
        public List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod, EngineCore engineCore) {
            final Predicate<Method> isConfigurationMethod = method -> {
                if (!method.isAnnotationPresent(ConfigureTestData.class)) {
                    return false;
                }

                if (method.getParameterCount() != 1) {
                    return false;
                }

                if (!method.getParameterTypes()[0].equals(TestSuiteBuilder.class)) {
                    return false;
                }

                ConfigureTestData configureTestData = method.getAnnotation(ConfigureTestData.class);
                if (configureTestData.suits().length == 0) {
                    return true;
                }
                return Arrays.binarySearch(configureTestData.suits(), annotation.suite()) >= 0;
            };

            return Arrays.stream(testClass.getMethods())
                    .filter(isConfigurationMethod)
                    .flatMap(it -> {
                        TestSuiteBuilder<Object, Object> testSuiteBuilder = new TestSuiteBuilder<>();
                        try {
                            it.invoke(null, testSuiteBuilder);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                            return Stream.empty();
                        }
                        return testSuiteBuilder.build().stream()
                                .map(entry -> new FactoryBasedTestCaseContent(testClass, entry, annotation));
                    })
                    .collect(Collectors.toList());
        }
    }, ANY {
        @Override
        public List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod, EngineCore engineCore) {
            final List<TestCaseContent> result = new ArrayList<>();
            final List<Throwable> encounteredExceptions = new ArrayList<>();
            final Consumer<SuiteType> extractAllFor = type -> {
                try {
                    result.addAll(type.supplyFor(annotation, testClass, testMethod, engineCore));
                } catch (Throwable t) {
                    encounteredExceptions.add(t);
                }
            };

            extractAllFor.accept(CLASSPATH_RESOURCE);
            extractAllFor.accept(FACTORY);
            extractAllFor.accept(CONFIGURATION_ON_TEST_CLASS);

            if (result.isEmpty()) {
                IllegalStateException exception = new IllegalStateException("Could not find sources for the suit " + annotation.suite() + "!");
                encounteredExceptions.forEach(exception::addSuppressed);
                encounteredExceptions.clear();
                throw exception;
            }

//            encounteredExceptions.forEach(Throwable::printStackTrace);
            encounteredExceptions.clear();
            return result;
        }
    };

    public abstract List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod, EngineCore engineCore);

    private static String checked(String input) {
        if (!input.endsWith("/")) {
            return input + "/";
        } else {
            return input;
        }
    }
}