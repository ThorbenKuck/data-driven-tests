package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.registry.TypeConverterAdapterRegistry;

public interface TestCaseOutput {

    String type();

    String rawName();

    <T> T convertTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry);
}
