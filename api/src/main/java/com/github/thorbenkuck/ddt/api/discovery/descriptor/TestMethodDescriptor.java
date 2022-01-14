package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.annotations.marker.TestInstanceTrigger;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMethodDescriptor implements DdtDescriptor {

    private final Method method;
    private final TestScenario annotation;
    private final List<TestCaseDescriptor> children = new ArrayList<>();
    private final ConfigurableTestContext context;
    private TestClassDescriptor parent;

    public static TestMethodDescriptor resolveWithChildren(EngineCore engineCore, Method method, Class<?> rootClass) {
        ConfigurableTestContext configurableTestContext = new ConfigurableTestContext(rootClass, engineCore);
        TestMethodDescriptor descriptor = new TestMethodDescriptor(method, AnnotationCollector.firstMatch(TestScenario.class, method), configurableTestContext);
        descriptor.resolveChildren();
        return descriptor;
    }

    public TestMethodDescriptor(Method method, TestScenario annotation, ConfigurableTestContext context) {
        this.method = method;
        this.annotation = annotation;
        this.context = context;
    }

    public void setParent(TestClassDescriptor testClassDescriptor) {
        this.parent = testClassDescriptor;
    }

    public TestClassDescriptor resolveParent() {
        TestClassDescriptor parent = new TestClassDescriptor(method.getDeclaringClass(), Collections.singletonList(method), context.getEngineCore());
        this.setParent(parent);
        return parent;
    }

    public void resolveChildren() {
        if(!children.isEmpty()) {
            return;
        }

        AtomicInteger index = new AtomicInteger(1);

        annotation.suiteType()
                .supplyFor(annotation, context.getTestClass(), method, context.getEngineCore())
                .stream()
                .map(caseContent -> new TestCaseDescriptor(
                        index.getAndIncrement(),
                        caseContent,
                        this,
                        context
                ))
                .forEach(children::add);
    }

    public List<TestCaseDescriptor> getChildren() {
        return children;
    }

    public String suggestedDisplayName() {
        return method.getName();
    }

    public TestClassDescriptor getParent() {
        return parent;
    }

    public Method testMethod() {
        return method;
    }

    @Override
    public void started() {
        context.setTestMethod(method);
        context.tryCreateTestInstance(TestInstanceTrigger.METHOD);
        context.getListeners().beforeMethod();
    }

    @Override
    public void ended(@NotNull TestResult result) {
        context.getListeners().afterMethod(result);
        context.tryTearDownTestInstance(TestInstanceTrigger.METHOD);
        context.setTestMethod(null);
    }

    @Override
    public ConfigurableTestContext getContext() {
        return context;
    }
}
