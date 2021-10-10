package com.github.thorbenkuck.ddt.execution.context;

import com.github.thorbenkuck.ddt.Annotations;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.TestScenarioListener;
import com.github.thorbenkuck.ddt.execution.ExecutionContext;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.domain.TestCaseLifecycleType;
import org.junit.platform.engine.TestExecutionResult;

import java.lang.reflect.Method;
import java.util.*;

public class ConfigurableExecutionContext implements ExecutionContext {

    private final Class<?> testClass;
    private final Method testMethod;
    private final TestScenario annotation;
    private final TestExecutionState testExecutionState;
    private final ExecutionContextAsserters asserterContext;
    private final ExecutionContextScenarioListeners scenarioListenerContext;
    private TestCaseContent currentContent = null;
    private TestExecutionResult result = null;

    public ConfigurableExecutionContext(
            Class<?> testCaseType,
            Method testSuiteMethod
    ) {
        this.testClass = testCaseType;
        this.testMethod = testSuiteMethod;
        this.annotation = Annotations.deepFindFirstIn(testSuiteMethod, TestScenario.class);
        testExecutionState = new TestExecutionState(testCaseType, TestCaseLifecycleType.valueOf(testClass, testMethod));
        asserterContext = new ExecutionContextAsserters(this.annotation, this.testClass, this.testMethod);
        scenarioListenerContext = new ExecutionContextScenarioListeners(testCaseType, testSuiteMethod);
    }

    /**
     * Returns the class, which holds the current TestCase
     *
     * @return
     */
    @Override
    public Class<?> getTestClass() {
        return testClass;
    }

    @Override
    public Method getTestMethod() {
        return testMethod;
    }

    @Override
    public TestScenario getAnnotation() {
        return annotation;
    }

    @Override
    public Optional<Object> getTestClassInstance() {
        return Optional.ofNullable(testExecutionState.getTestInstance());
    }

    @Override
    public Object requireTestClassInstance() {
        return getTestClassInstance().orElseThrow(() -> new IllegalStateException("No test class instance present"));
    }

    @Override
    public Optional<TestCaseContent> getCurrentContent() {
        return Optional.ofNullable(currentContent);
    }

    @Override
    public TestCaseContent requireCurrentContent() {
        return getCurrentContent().orElseThrow(() -> new IllegalStateException("No test content set"));
    }

    @Override
    public TestExecutionState getTestExecutionState() {
        return testExecutionState;
    }

    @Override
    public Optional<TestExecutionResult> getResult() {
        return Optional.ofNullable(result);
    }

    @Override
    public TestExecutionResult requireResult() {
        return getResult().orElseThrow(() -> new IllegalStateException("No TestExecutionResult is present"));
    }

    public void setCurrentContent(TestCaseContent currentContent) {
        this.currentContent = currentContent;
    }

    public List<TestScenarioListener> getScenarioListeners() {
        return scenarioListenerContext.getScenarioListeners();
    }

    public List<Asserter> getAsserters() {
        return asserterContext.getAsserters();
    }

    public void setResult(TestExecutionResult result) {
        this.result = result;
    }
}
