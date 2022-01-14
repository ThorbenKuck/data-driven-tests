package com.github.thorbenkuck.ddt.api.annotations.importer.meta;

import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.api.annotations.importer.TestCaseFactories;
import com.github.thorbenkuck.ddt.api.annotations.importer.TestContentFactory;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;

import java.lang.reflect.Method;

public class TestCaseFactoryMetaData {

    private final OrderedSet<Class<? extends TestCaseFactory<?, ?>>> factories;

    public TestCaseFactoryMetaData(OrderedSet<Class<? extends TestCaseFactory<?, ?>>> factories) {
        this.factories = factories;
    }

    public static TestCaseFactoryMetaData of(Class<?> testClass, Method... annotatedElements) {
        return AnnotationCollector.of(TestContentFactory.class)
                .searchIn(annotatedElements)
                .searchIn(testClass)
                .analyze()
                .<Class<? extends TestCaseFactory<?, ?>>>map(TestContentFactory::value)
                .flatAppendAllFor(TestCaseFactories.class, TestCaseFactories::value)
                .flatAppendAllFor(TestScenario.class, TestScenario::factories)
                .sinkInto(TestCaseFactoryMetaData::new);
    }

    public OrderedSet<Class<? extends TestCaseFactory<?, ?>>> getFactories() {
        return factories;
    }
}
