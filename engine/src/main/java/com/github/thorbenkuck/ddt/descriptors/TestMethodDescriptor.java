package com.github.thorbenkuck.ddt.descriptors;

import com.github.thorbenkuck.ddt.api.domain.TestScenarioListener;
import com.github.thorbenkuck.ddt.execution.LifeCycle;
import com.github.thorbenkuck.ddt.execution.context.ConfigurableExecutionContext;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMethodDescriptor extends AbstractTestDescriptor implements DataDrivenDescriptor {

    private final ConfigurableExecutionContext context;
    private final TestClassDescriptor parent;

    public TestMethodDescriptor(TestClassDescriptor testClassDescriptor, Class<?> root, Method method) {
        super(uniqueId(testClassDescriptor, method), displayName(method), MethodSource.from(root, method));
        parent = testClassDescriptor;
        setParent(parent);
        this.context = new ConfigurableExecutionContext(root, method);
    }

    private static UniqueId uniqueId(TestClassDescriptor testClassDescriptor, Method method) {
        return testClassDescriptor.getUniqueId().append("method", method.getName());
    }

    private static String displayName(Method method) {
        return method.getName();
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    @Override
    public TestMethodDescriptor resolve() {
        AtomicInteger index = new AtomicInteger(1);
        context.getAnnotation().suiteType()
                .supplyFor(context.getAnnotation(), context.getTestClass(), context.getTestMethod())
                .forEach(testCaseContent -> {
                    String name = testCaseContent.input().testName(index.getAndIncrement(), context.getAnnotation());
                    TestSource testSource = testCaseContent.testSource();
                    UniqueId id = getUniqueId().append("testcase", index + testCaseContent.input().fullPath());
                    TestCaseDescriptor testCaseDescriptor = new TestCaseDescriptor(id, name, testSource, testCaseContent, context);
                    testCaseDescriptor.setParent(this);
                    addChild(testCaseDescriptor);
                });
        return this;
    }

    @Override
    public void started() {
        parent.started(context);
        context.getTestExecutionState().testMethodReached();
        context.getScenarioListeners().forEach(TestScenarioListener::beforeMethod);
        context.getTestClassInstance().ifPresent(LifeCycle::executeBeforeScenario);
    }

    @Override
    public void ended() {
        context.getScenarioListeners().forEach(TestScenarioListener::afterMethod);
        context.getTestClassInstance().ifPresent(LifeCycle::executeAfterScenario);
        context.getTestExecutionState().testMethodTeardown();
        parent.ended(context);
    }
}
