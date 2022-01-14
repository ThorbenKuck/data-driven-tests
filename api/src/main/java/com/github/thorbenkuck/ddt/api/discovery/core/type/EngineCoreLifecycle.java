package com.github.thorbenkuck.ddt.api.discovery.core.type;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.ExecutionContext;

public interface EngineCoreLifecycle {

    default void beforeTestClass(ExecutionContext context) {}

    default void beforeTestMethod(ExecutionContext context) {}

    default void beforeTestCase(ExecutionContext context) {}

    default void afterTestCase(ExecutionContext context) {}

    default void afterTestMethod(ExecutionContext context) {}

    default void afterTestClass(ExecutionContext context) {}

}
