package com.github.thorbenkuck.ddt.descriptors;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.execution.LifeCycle;
import com.github.thorbenkuck.ddt.execution.context.ConfigurableExecutionContext;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;

import java.lang.reflect.Method;

public class TestCaseDescriptor extends AbstractTestDescriptor implements DataDrivenDescriptor {

    private final TestCaseContent testCaseFile;
    private final ConfigurableExecutionContext context;

    protected TestCaseDescriptor(
            UniqueId engineId,
            String name,
            TestSource testSource,
            TestCaseContent testCaseContent,
            ConfigurableExecutionContext context
    ) {
        super(engineId, name, testSource);
        this.testCaseFile = testCaseContent;
        this.context = context;
    }

    @Override
    public TestDescriptor.Type getType() {
        return Type.TEST;
    }

    public Method getMethod() {
        return context.getTestMethod();
    }

    public Class<?> getRoot() {
        return context.getTestClass();
    }

    public TestCaseContent getTestFiles() {
        return testCaseFile;
    }

    public TestScenario getAnnotation() {
        return context.getAnnotation();
    }

    public ConfigurableExecutionContext getContext() {
        return this.context;
    }

    @Override
    public void started() {
        context.setCurrentContent(testCaseFile);
        context.getTestExecutionState().testCaseReached();
        context.getScenarioListeners().forEach(listener -> listener.beforeCase(context.requireTestClassInstance()));
        context.getTestClassInstance().ifPresent(LifeCycle::executeBeforeEach);
    }

    @Override
    public void ended() {
        context.getScenarioListeners().forEach(listener -> listener.afterCase(context.requireTestClassInstance(), context.requireResult()));
        context.getTestClassInstance().ifPresent(LifeCycle::executeAfterEach);
        context.getTestExecutionState().testCaseTeardown();
    }
}
