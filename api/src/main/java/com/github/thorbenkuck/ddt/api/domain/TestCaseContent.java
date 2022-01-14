package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.registry.TypeConverterAdapterRegistry;
import org.junit.platform.engine.TestSource;

import java.lang.reflect.Method;
import java.util.List;

public interface TestCaseContent {

    void addPreconditions(List<Object> preconditions);

    String rawName();

    TestSource testSource();

    void validateMatchesMethod(Method method);

    <T> T convertInputTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry);

    <T> T convertOutputTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry);

    TestCaseOutput output();

    TestCaseInput input();

    List<Object> preconditions();

    default List<TestCaseContent> subContent(EngineCore scenarioContext) {
        throw new UnsupportedOperationException("Sub content not supported by default");
    }

}
