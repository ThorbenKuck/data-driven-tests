package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.services.adapter.TypeConverterAdapterRegistry;

public interface TestCaseOutput {

    String type();

    String rawName();

    <T> T convertTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry);
}
