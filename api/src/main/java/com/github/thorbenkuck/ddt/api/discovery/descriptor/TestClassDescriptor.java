package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import com.github.thorbenkuck.ddt.api.annotations.marker.TestInstanceTrigger;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.identification.IsTestMethodPredicate;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestClassDescriptor implements DdtDescriptor {

    private final Class<?> root;
    private final List<Method> methods;
    private final EngineCore engineCore;
    private final List<TestMethodDescriptor> children = new ArrayList<>();
    private final ConfigurableTestContext context;

    public TestClassDescriptor(Class<?> root, List<Method> methods, EngineCore engineCore) {
        if (methods.isEmpty()) {
            throw new IllegalArgumentException("A TestClassDescriptor requires at minimum one method to execute.");
        }
        this.root = root;
        this.methods = methods;
        this.engineCore = engineCore;
        this.context = new ConfigurableTestContext(root, engineCore);

        resolveChildren();
    }

    public static Optional<TestClassDescriptor> resolveWithChildren(EngineCore engineCore, Class<?> root) {
        List<Method> testMethods = Arrays.stream(root.getMethods())
                .filter(IsTestMethodPredicate.get())
                .collect(Collectors.toList());

        if(testMethods.isEmpty()) {
            return Optional.empty();
        }

        TestClassDescriptor descriptor = new TestClassDescriptor(
                root,
                testMethods,
                engineCore
        );

        descriptor.resolveChildren();
        return Optional.of(descriptor);
    }

    public void add(TestMethodDescriptor testMethodDescriptor) {
        children.add(testMethodDescriptor);
    }

    private void resolveChildren() {
        if(!children.isEmpty()) {
            return;
        }
        methods.stream()
                .filter(IsTestMethodPredicate.get())
                .forEach(method -> {
                    TestMethodDescriptor testMethodDescriptor = TestMethodDescriptor.resolveWithChildren(engineCore, method, root);
                    testMethodDescriptor.resolveChildren();
                    testMethodDescriptor.setParent(this);
                    add(testMethodDescriptor);
                });

    }

    public List<TestMethodDescriptor> getChildren() {
        return children;
    }

    public String suggestedDisplayName() {
        return root.getSimpleName();
    }

    public Class<?> testClass() {
        return root;
    }

    @Override
    public void started() {
        context.tryCreateTestInstance(TestInstanceTrigger.CLASS);
        context.getListeners().beforeClass();
        context.getEngineCore().beforeTestClass(context);
    }

    @Override
    public void ended(@NotNull TestResult testTestResult) {
        context.getEngineCore().afterTestClass(context);
        context.getListeners().afterClass(testTestResult);
        context.tryTearDownTestInstance(TestInstanceTrigger.CLASS);
    }

    @Override
    public ConfigurableTestContext getContext() {
        return context;
    }
}
