package com.github.thorbenkuck.ddt.api.discovery.core.type;

import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.api.domain.listener.TestScenarioListener;
import com.github.thorbenkuck.ddt.api.domain.TypeConverterAdapter;
import com.github.thorbenkuck.ddt.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface EngineCoreInjector {

    // ###################
    // # General Methods #
    // ###################
    <T> T createInstanceOf(Class<T> type);

    default void postConstruct() {
    }

    default void injectDependencies(Object o) {
    }

    default <T> T getInstanceOf(Class<T> type) {
        return createInstanceOf(type);
    }

    default Object[] resolveParameters(Method method) {
        if(method.getParameterCount() == 0) {
            return new Object[] {};
        }
        final List<Object> parameters = new ArrayList<>();
        for (Class<?> parameterType : method.getParameterTypes()) {
            parameters.add(getInstanceOf(parameterType));
        }
        return parameters.toArray();
    }

    // #######################
    // # Specialized Methods #
    // #######################
    default <T> T createTestInstance(Class<T> scenarioType) {
        return createInstanceOf(scenarioType);
    }

    default <T extends TestScenarioListener> T createListenerInstance(Class<T> scenarioType) {
        return createInstanceOf(scenarioType);
    }

    /**
     * Will only be called for classes, that are not created by this factory
     *
     * @param asserter the Asserter, that has been created by a ServiceLoader.
     */
    default void injectIntoAsserter(Asserter asserter) {
        injectDependencies(asserter);
    }

    /**
     * Will only be called for classes, that are not created by this factory
     *
     * @param typeConverterAdapter the TypeConverterAdapter, that has been created by a ServiceLoader.
     */
    default void injectIntoTypeConverterAdapter(TypeConverterAdapter typeConverterAdapter) {
        injectDependencies(typeConverterAdapter);
    }

    default void injectIntoScenarioListener(TestScenarioListener testScenarioListener) {
        injectDependencies(testScenarioListener);
    }

    default <T> void tearDown(T instance) {}

    default TestCaseFactory<?, ?> createCaseFactoryInstance(Class<? extends TestCaseFactory> aClass) {
        return createInstanceOf(aClass);
    }

    default void adjustToTestCase(Class<?> testCase) {
        Reflections.invokeInheritedMethods(this, testCase);
    }

}
