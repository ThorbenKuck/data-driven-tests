package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.services.adapter.TypeConverterAdapterRegistry;
import org.junit.platform.engine.TestSource;

import java.lang.reflect.Method;

public interface TestCaseContent {

    String rawName();

    TestSource testSource();

    void validateMatchesMethod(Method method);

    <T> T convertInputTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry);

    <T> T convertOutputTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry);

    TestCaseOutput output();

    TestCaseInput input();

}
