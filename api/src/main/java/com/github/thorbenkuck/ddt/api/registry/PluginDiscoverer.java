package com.github.thorbenkuck.ddt.api.registry;

import com.github.thorbenkuck.ddt.api.domain.listener.ContextFreeScenarioListener;
import com.github.thorbenkuck.ddt.properties.DdtProperties;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.api.domain.TypeConverterAdapter;
import io.github.classgraph.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PluginDiscoverer {

    private static TypeConverterAdapterRegistry TYPE_CONVERTER_ADAPATER_INSTANCE = new TypeConverterAdapterRegistry();
    private static TestContentFactoryRegistry TEST_CONTENT_FACTORY_INSTANCE = new TestContentFactoryRegistry();
    private static AsserterRegistry ASSERTER_INSTANCE = new AsserterRegistry();
    private static ContextFreeScenarioListenerRegistry LISTENER_REGISTRY = new ContextFreeScenarioListenerRegistry();
    private static Supplier<Optional<EngineCore>> defaultTestScenarioSupplier;

    private static Optional<EngineCore> returnChecked(@Nullable EngineCore context) {
        if(context == null) {
            defaultTestScenarioSupplier = Optional::empty;
            return Optional.empty();
        } else {
            if(context.singleton()) {
                defaultTestScenarioSupplier = () -> Optional.of(context);
            }
            return Optional.of(context);
        }
    }

    /**
     * Supports field injection.
     *
     */
    public static synchronized Optional<EngineCore> loadDefaultTestScenarioContext() {
        if(defaultTestScenarioSupplier != null) {
            return defaultTestScenarioSupplier.get();
        }

        List<EngineCore> engineCores = loadAllWithDefaultInstantiationFor(EngineCore.class);
        if(engineCores.isEmpty()) {
            return returnChecked(null);
        } else if(engineCores.size() > 1) {
            Optional<String> activeContext = DdtProperties.activeTestScenarioContext();
            if(activeContext.isPresent()) {
                List<EngineCore> collect = engineCores.stream()
                        .filter(it -> it.getClass().getName().equals(activeContext.get()))
                        .collect(Collectors.toList());
                if(collect.size() != 1) {
                    collect = engineCores.stream()
                            .filter(it -> it.getClass().getSimpleName().equals(activeContext.get()))
                            .collect(Collectors.toList());
                    if(collect.size() == 1) {
                        return returnChecked(collect.get(0));
                    } else {
                        System.err.println("Conflict for default TestScenarioContext! It is highly suggested you fix this!");
                        System.err.println("You have the following active context set in the properties: " + activeContext.get());
                        System.err.println("But we could not uniquely identify one instance to use!");
                        System.err.println("All TestScenarioContext in your classpath: [" + engineCores.stream().map(it -> it.getClass().getName()).collect(Collectors.joining(", ")) + "]");
                        if(collect.size() > 1) {
                            EngineCore engineCore = collect.get(0);
                            System.err.println("We will continue using the following TestScenarioContext: " + engineCore);
                            return returnChecked(engineCore);
                        } else {
                            return returnChecked(null);
                        }
                    }
                } else {
                    return returnChecked(collect.get(0));
                }
            }
            EngineCore engineCore = engineCores.get(0);
            System.err.println("Conflict for default TestScenarioContext! It is highly suggested you fix this!");
            System.err.println("All TestScenarioContext in your classpath: [" + engineCores.stream().map(it -> it.getClass().getName()).collect(Collectors.joining(", ")) + "]");
            System.err.println("[Recover]: Continuing with the following context: " + engineCore);
            return returnChecked(engineCore);
        } else {
            return returnChecked(engineCores.get(0));
        }
    }

    public static synchronized List<ContextFreeScenarioListener> loadContextFreeScenarioListeners() {
        if(LISTENER_REGISTRY.isSetup()) {
            return LISTENER_REGISTRY.access();
        }

        final List<ContextFreeScenarioListener> listeners = loadAllWithDefaultInstantiationFor(ContextFreeScenarioListener.class);
        listeners.forEach(LISTENER_REGISTRY::register);
        LISTENER_REGISTRY.setupDone();
        return LISTENER_REGISTRY.access();
    }

    /**
     * Supports field injection.
     *
     * @param engineCore
     * @return
     */
    public static synchronized TypeConverterAdapterRegistry loadTypeConverterAdapter(EngineCore engineCore) {
        if(TYPE_CONVERTER_ADAPATER_INSTANCE.isSetup()) {
            return TYPE_CONVERTER_ADAPATER_INSTANCE;
        }

        final List<TypeConverterAdapter> loadedConverters = loadAllFor(TypeConverterAdapter.class, engineCore);

        loadedConverters.stream()
                .sorted(Comparator.comparingInt(TypeConverterAdapter::order))
                .forEachOrdered(adapter -> {
                    engineCore.injectIntoTypeConverterAdapter(adapter);
                    TYPE_CONVERTER_ADAPATER_INSTANCE.register(adapter);
                });

        TYPE_CONVERTER_ADAPATER_INSTANCE.setupDone();
        return TYPE_CONVERTER_ADAPATER_INSTANCE;
    }

    /**
     * Supports field injection.
     *
     * @param engineCore
     * @return
     */
    public static synchronized AsserterRegistry loadAllAsserters(EngineCore engineCore) {
        if(ASSERTER_INSTANCE.isSetup()) {
            return ASSERTER_INSTANCE;
        }

        loadAllFor(Asserter.class, engineCore)
                .forEach(asserter -> {
                    engineCore.injectIntoAsserter(asserter);
                    ASSERTER_INSTANCE.register(asserter);
                });

        ASSERTER_INSTANCE.setupDone();
        return ASSERTER_INSTANCE;
    }

    public static synchronized TestContentFactoryRegistry loadTestContentFactories(EngineCore engineCore) {
        if(TEST_CONTENT_FACTORY_INSTANCE.isSetup()) {
            return TEST_CONTENT_FACTORY_INSTANCE;
        }

        final List<TestCaseFactory> loadedConverters = loadAllFor(TestCaseFactory.class, engineCore);
        loadedConverters.forEach(engineCore::injectDependencies);

        Consumer<List<Class<? extends TestCaseFactory>>> instantiateAll = list -> {
            list.stream()
                    .map(engineCore::createCaseFactoryInstance)
                    .forEach(loadedConverters::add);
        };

        if(DdtProperties.contentFactoryClassPathScanningEnabled()) {
            DdtProperties.contentFactoryClassPathBasePackage()
                    .ifPresent(basePackage -> instantiateAll.accept(findInClassPathSubPackage(TestCaseFactory.class, basePackage)))
                    .orElse(() -> instantiateAll.accept(findInClassPath(TestCaseFactory.class)));
        }

        loadedConverters.forEach(TEST_CONTENT_FACTORY_INSTANCE::register);
        TEST_CONTENT_FACTORY_INSTANCE.setupDone();

        return TEST_CONTENT_FACTORY_INSTANCE;
    }

    private static <T> List<T> loadAllFor(Class<T> type, EngineCore context) {
        final List<T> result = new ArrayList<>();

        try {
            ServiceLoader.load(type)
                    .stream()
                    .map(ServiceLoader.Provider::type)
                    .map(context::createInstanceOf)
                    .forEach(result::add);

            ServiceLoader.loadInstalled(type)
                    .stream()
                    .map(ServiceLoader.Provider::type)
                    .map(context::createInstanceOf)
                    .forEach(result::add);
        } catch (Exception e) {
            ServiceLoader.load(type)
                    .forEach(result::add);
            ServiceLoader.loadInstalled(type)
                    .forEach(result::add);

            result.forEach(context::injectDependencies);
        }

        return result.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static <T> List<T> loadAllWithDefaultInstantiationFor(Class<T> type) {
        final List<T> result = new ArrayList<>();

        ServiceLoader.load(type)
                .forEach(result::add);
        ServiceLoader.loadInstalled(type)
                .forEach(result::add);

        return result.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static <T> List<Class<? extends T>> findInClassPath(Class<T> interfaceType) {
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().scan()) {
            return scanResult.getClassesImplementing(interfaceType)
                    .stream()
                    .map(it -> it.loadClass(interfaceType))
                    .collect(Collectors.toList());
        }
    }

    private static <T> List<Class<? extends T>> findInClassPathSubPackage(Class<T> interfaceType, String basePackage) {
        try (ScanResult scanResult = new ClassGraph().verbose().acceptPackages(basePackage).scan()) {
            return scanResult.getClassesImplementing(interfaceType)
                    .stream()
                    .map(it -> it.loadClass(interfaceType))
                    .collect(Collectors.toList());
        }
    }
}
