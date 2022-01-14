package com.github.thorbenkuck.ddt.api.domain.listener;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.ConfigurableTestContext;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import com.github.thorbenkuck.ddt.collection.OrderedSet;

public class TestScenarioListenerContext {

    private final OrderedSet<TestScenarioListener> listeners;
    private final ConfigurableTestContext context;

    public TestScenarioListenerContext(ConfigurableTestContext context) {
        this.listeners = TestScenarioListener.findAll(context.getTestClass(), context.getEngineCore())
                .stream()
                .sorted()
                .collect(OrderedSet.collector());
        listeners.add(new ReflectiveTestScenarioListener(context.getTestClass()));
        this.context = context;
    }

    public boolean addListener(TestScenarioListener testScenarioListener) {
        return listeners.add(testScenarioListener);
    }

    public void beforeClass() {
        context.getEngineCore().beforeTestClass(context);
        listeners.forEach(listener -> listener.beforeClass(context));
    }

    public void beforeMethod() {
        context.getEngineCore().beforeTestMethod(context);
        listeners.forEach(listener -> listener.beforeMethod(context));
    }

    public void beforeCase() {
        context.getEngineCore().beforeTestCase(context);
        listeners.forEach(listener -> listener.beforeCase(context));
    }

    public void afterCase(TestResult result) {
        context.getEngineCore().afterTestCase(context);
        listeners.forEach(listeners -> listeners.afterCase(context, result));
    }

    public void afterMethod(TestResult result) {
        context.getEngineCore().afterTestMethod(context);
        listeners.forEach(listeners -> listeners.afterMethod(context, result));
    }

    public void afterClass(TestResult result) {
        context.getEngineCore().afterTestClass(context);
        listeners.forEach(listeners -> listeners.afterClass(context, result));
    }
}
