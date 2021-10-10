package com.github.thorbenkuck.ddt.api.domain.factory;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.TestCaseInput;
import com.github.thorbenkuck.ddt.api.services.adapter.TypeConverterAdapterRegistry;

public class FactoryBasedTestCaseInput<T, S> implements TestCaseInput {

    private final T t;
    private final TestCaseEntry<T, S> factoryResult;

    public FactoryBasedTestCaseInput(TestCaseEntry<T, S> factoryResult) {
        this.factoryResult = factoryResult;
        this.t = factoryResult.getInput();
    }

    @Override
    public String type() {
        return "class";
    }

    @Override
    public String rawName() {
        return factoryResult.getName();
    }

    @Override
    public String fullPath() {
        return null;
    }

    @Override
    public <R> R convertTo(Class<R> type, TypeConverterAdapterRegistry adapterRegistry) {
        if(type.isAssignableFrom(t.getClass())) {
            return (R) t;
        } else {
            throw new IllegalStateException("Cannot assign " + type + " to " + t.getClass());
        }
    }

    @Override
    public String testName(int index, TestScenario annotation) {
        return "[" + index + "] " + rawName();
    }

    public Class<?> classType() {
        return t.getClass();
    }

    public TestCaseEntry<T, S> getFactoryResult() {
        return factoryResult;
    }
}
