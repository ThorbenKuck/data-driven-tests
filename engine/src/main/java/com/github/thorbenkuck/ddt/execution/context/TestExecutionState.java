package com.github.thorbenkuck.ddt.execution.context;

import com.github.thorbenkuck.ddt.Reflections;
import com.github.thorbenkuck.ddt.execution.TestMethodExecutor;
import com.github.thorbenkuck.ddt.api.domain.TestCaseLifecycleType;
import org.junit.platform.engine.TestExecutionResult;

public class TestExecutionState {

    private final Class<?> type;
    private final TestCaseLifecycleType lifecycleType;
    private final TestMethodExecutor testMethodExecutor = new TestMethodExecutor();
    private Object testInstance = null;

    public TestExecutionState(Class<?> type, TestCaseLifecycleType lifecycleType) {
        this.type = type;
        this.lifecycleType = lifecycleType;
    }

    public void testClassReached() {
        if (lifecycleType == TestCaseLifecycleType.PER_CLASS) {
            testInstance = Reflections.newInstance(type);
        }
    }

    public void testClassFinished() {
        if (lifecycleType == TestCaseLifecycleType.PER_CLASS) {
            testInstance = null; // TODO TearDown and stuff
        }
    }

    public void testMethodReached() {
        if (lifecycleType == TestCaseLifecycleType.PER_METHOD) {
            testInstance = Reflections.newInstance(type);
        }
    }

    public void testMethodTeardown() {
        if (lifecycleType == TestCaseLifecycleType.PER_METHOD) {
            testInstance = null; // TODO TearDown and stuff
        }
    }

    public void testCaseReached() {
        if (lifecycleType == TestCaseLifecycleType.PER_CASE) {
            testInstance = Reflections.newInstance(type);
        }
    }

    public void testCaseTeardown() {
        if (lifecycleType == TestCaseLifecycleType.PER_CASE) {
            testInstance = null; // TODO TearDown and stuff
        }
    }

    public TestExecutionResult executeTest(ConfigurableExecutionContext executionContext, String displayName) {
        executionContext.requireCurrentContent()
                .validateMatchesMethod(executionContext.getTestMethod());
        TestExecutionResult testExecutionResult = testMethodExecutor.invokeTestMethod(executionContext, testInstance, displayName);
        executionContext.setResult(testExecutionResult);
        return testExecutionResult;
    }

    public Object getTestInstance() {
        return testInstance;
    }
}
