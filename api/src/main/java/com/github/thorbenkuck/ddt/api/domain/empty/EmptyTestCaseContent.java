package com.github.thorbenkuck.ddt.api.domain.empty;

import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.TestCaseInput;
import com.github.thorbenkuck.ddt.api.domain.TestCaseOutput;
import com.github.thorbenkuck.ddt.api.registry.TypeConverterAdapterRegistry;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class EmptyTestCaseContent implements TestCaseContent {

    public final Method method;

    public EmptyTestCaseContent(Method method) {
        this.method = method;
    }

    @Override
    public void addPreconditions(List<Object> preconditions) {

    }

    @Override
    public String rawName() {
        return method.getName();
    }

    @Override
    public TestSource testSource() {
        return MethodSource.from(method);
    }

    @Override
    public void validateMatchesMethod(Method method) {
        if(method.getParameterCount() != 0) {
            throw new IllegalStateException("EmptyTestCaseContent may only be used for methods with 0 parameters. Used for method " + method);
        }
    }

    @Override
    public <T> T convertInputTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry) {
        return null;
    }

    @Override
    public <T> T convertOutputTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry) {
        return null;
    }

    @Override
    public TestCaseOutput output() {
        return null;
    }

    @Override
    public TestCaseInput input() {
        return null;
    }

    @Override
    public List<Object> preconditions() {
        return Collections.emptyList();
    }
}
