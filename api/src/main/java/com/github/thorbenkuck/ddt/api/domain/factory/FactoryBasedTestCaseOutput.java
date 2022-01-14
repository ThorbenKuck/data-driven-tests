package com.github.thorbenkuck.ddt.api.domain.factory;

import com.github.thorbenkuck.ddt.api.domain.TestCaseOutput;
import com.github.thorbenkuck.ddt.api.registry.TypeConverterAdapterRegistry;

public class FactoryBasedTestCaseOutput<S> implements TestCaseOutput {

    private final S s;

    public FactoryBasedTestCaseOutput(S s) {
        this.s = s;
    }

    @Override
    public String type() {
        return null;
    }

    @Override
    public String rawName() {
        return s.getClass().getSimpleName();
    }

    public Class<?> classType() {
        return s.getClass();
    }

    @Override
    public <T> T convertTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry) {
        if(type.isAssignableFrom(s.getClass())) {
            return (T) s;
        } else {
            throw new IllegalStateException("Cannot assign " + type + " to " + s.getClass());
        }
    }
}
