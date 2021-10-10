package com.github.thorbenkuck.ddt.api.domain;

import org.junit.platform.engine.TestExecutionResult;

public interface TestScenarioListener {

    default void beforeMethod() {}
    default void beforeCase(Object testInstance) {}
    default void afterCase(Object testInstance, TestExecutionResult result) {}
    default void afterMethod() {}

}
