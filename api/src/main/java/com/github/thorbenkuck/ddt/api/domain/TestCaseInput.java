package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.registry.TypeConverterAdapterRegistry;

public interface TestCaseInput {

    String type();

    String rawName();

    String fullPath();

    <T> T convertTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry);

    String testName(int index, TestScenario annotation);
}
