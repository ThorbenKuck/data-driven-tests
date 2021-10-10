package com.github.thorbenkuck.ddt.api;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.DdtProperties;
import com.github.thorbenkuck.ddt.api.domain.TestContentFactory;
import com.github.thorbenkuck.ddt.api.domain.classpath.PathBasedTestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.factory.FactoryBaseTestCaseContent;
import com.github.thorbenkuck.ddt.api.services.PluginDiscoverer;
import com.github.thorbenkuck.ddt.api.services.factory.TestContentFactoryRegistry;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum SuiteType {
    CLASSPATH_RESOURCE {
        @Override
        public List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod) {
            if (annotation.suite().trim().isEmpty()) {
                throw new IllegalStateException("For the SuiteLocation CLASSPATH_RESOURCE, you'll have to provide a present folder in the \"suite\" field");
            }

            String suiteFolderInClassPath = checked(DdtProperties.suitsRoot()) + annotation.suite();
            URL rootFolderResource = getClass().getClassLoader().getResource(suiteFolderInClassPath);
            if (rootFolderResource != null) {
                try {
                    return Files.list(Paths.get(rootFolderResource.toURI()))
                            .filter(path -> !path.getFileName().toString().contains("." + annotation.expectedFlag()))
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
        public List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod) {
            final TestContentFactoryRegistry testContentFactoryRegistry = PluginDiscoverer.loadTestContentFactories();
            List<TestContentFactory<?, ?>> suiteTestContentFactories = testContentFactoryRegistry.findAllForSuite(annotation.suite());
            if (suiteTestContentFactories.isEmpty()) {
                throw new IllegalStateException("No TestCaseContentFactories registered for the suite " + annotation.suite());
            }

            List<TestCaseContent> result = suiteTestContentFactories.stream()
                    .flatMap(factory -> {
                        Class<? extends TestContentFactory> factoryType = factory.getClass();
                        return factory.stream()
                                .map(entry -> new FactoryBaseTestCaseContent(factoryType, entry, annotation));

                    })
                    .collect(Collectors.toList());

            if (result.isEmpty()) {
                throw new IllegalStateException("No TestContentFactory for the suite " + annotation.suite() + " produced any scenarios!");
            }
            return result;
        }
    }, ANY {
        @Override
        public List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod) {
            final List<TestCaseContent> result = new ArrayList<>();
            final List<Throwable> encounteredExceptions = new ArrayList<>();

            try {
                result.addAll(CLASSPATH_RESOURCE.supplyFor(annotation, testClass, testMethod));
            } catch (Throwable t) {
                encounteredExceptions.add(t);
            }

            try {
                result.addAll(FACTORY.supplyFor(annotation, testClass, testMethod));
            } catch (Throwable t) {
                encounteredExceptions.add(t);
            }

            if (result.isEmpty()) {
                IllegalStateException exception = new IllegalStateException("Could not find sources for the suit " + annotation.suite() + "!");
                encounteredExceptions.forEach(exception::addSuppressed);
                encounteredExceptions.clear();
                throw exception;
            }
            return result;
        }
    };

    public abstract List<TestCaseContent> supplyFor(TestScenario annotation, Class<?> testClass, Method testMethod);

    private static String checked(String input) {
        if (!input.endsWith("/")) {
            return input + "/";
        } else {
            return input;
        }
    }
}