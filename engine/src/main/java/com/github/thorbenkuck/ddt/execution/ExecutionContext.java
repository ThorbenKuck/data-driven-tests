package com.github.thorbenkuck.ddt.execution;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.execution.context.TestExecutionState;
import org.junit.platform.engine.TestExecutionResult;

import java.lang.reflect.Method;
import java.util.Optional;

public interface ExecutionContext {
    Class<?> getTestClass();

    Method getTestMethod();

    TestScenario getAnnotation();

    Optional<Object> getTestClassInstance();

    Object requireTestClassInstance();

    Optional<TestCaseContent> getCurrentContent();

    TestCaseContent requireCurrentContent();

    TestExecutionState getTestExecutionState();

    Optional<TestExecutionResult> getResult();

    TestExecutionResult requireResult();
}
