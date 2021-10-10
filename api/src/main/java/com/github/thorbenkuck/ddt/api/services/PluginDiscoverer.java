package com.github.thorbenkuck.ddt.api.services;

import com.github.thorbenkuck.ddt.Reflections;
import com.github.thorbenkuck.ddt.DdtProperties;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.domain.TestContentFactory;
import com.github.thorbenkuck.ddt.api.domain.TypeConverterAdapter;
import com.github.thorbenkuck.ddt.api.services.adapter.TypeConverterAdapterRegistry;
import com.github.thorbenkuck.ddt.api.services.asserter.AsserterRegistry;
import com.github.thorbenkuck.ddt.api.services.factory.TestContentFactoryRegistry;
import io.github.classgraph.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PluginDiscoverer {

    private static TypeConverterAdapterRegistry TYPE_CONVERTER_ADAPATER_INSTANCE = new TypeConverterAdapterRegistry();
    private static TestContentFactoryRegistry TEST_CONTENT_FACTORY_INSTANCE = new TestContentFactoryRegistry();
    private static AsserterRegistry ASSERTER_INSTANCE = new AsserterRegistry();

    public static synchronized TypeConverterAdapterRegistry loadTypeConverterAdapter() {
        if(TYPE_CONVERTER_ADAPATER_INSTANCE.isSetup()) {
            return TYPE_CONVERTER_ADAPATER_INSTANCE;
        }

        final List<TypeConverterAdapter> loadedConverters = loadAllFor(TypeConverterAdapter.class);

        loadedConverters.stream()
                .sorted(Comparator.comparingInt(TypeConverterAdapter::order))
                .forEachOrdered(adapter -> TYPE_CONVERTER_ADAPATER_INSTANCE.register(adapter));

        TYPE_CONVERTER_ADAPATER_INSTANCE.setupDone();
        return TYPE_CONVERTER_ADAPATER_INSTANCE;
    }

    public static synchronized TestContentFactoryRegistry loadTestContentFactories() {
        if(TEST_CONTENT_FACTORY_INSTANCE.isSetup()) {
            return TEST_CONTENT_FACTORY_INSTANCE;
        }

        final List<TestContentFactory> loadedConverters = loadAllFor(TestContentFactory.class);

        Consumer<List<Class<? extends TestContentFactory>>> instantiateAll = list -> {
            list.stream()
                    .map(Reflections::newInstance)
                    .forEach(loadedConverters::add);
        };

        if(DdtProperties.contentFactoryClassPathScanningEnabled()) {
            DdtProperties.contentFactoryClassPathBasePackage()
                    .ifPresent(basePackage -> instantiateAll.accept(findInClassPathSubPackage(TestContentFactory.class, basePackage)))
                    .orElse(() -> instantiateAll.accept(findInClassPath(TestContentFactory.class)));
        }

        loadedConverters.forEach(adapter -> {
            TEST_CONTENT_FACTORY_INSTANCE.register(adapter);
        });

        TEST_CONTENT_FACTORY_INSTANCE.setupDone();
        return TEST_CONTENT_FACTORY_INSTANCE;
    }

    public static synchronized AsserterRegistry loadAllAsserters() {
        if(ASSERTER_INSTANCE.isSetup()) {
            return ASSERTER_INSTANCE;
        }

        loadAllFor(Asserter.class)
                .forEach(ASSERTER_INSTANCE::register);

        ASSERTER_INSTANCE.setupDone();
        return ASSERTER_INSTANCE;
    }

    public static <T> List<T> loadAllFor(Class<T> type) {
        final List<T> result = new ArrayList<>();

        ServiceLoader.load(type)
                .forEach(result::add);
        ServiceLoader.loadInstalled(type)
                .forEach(result::add);

        return result;
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
