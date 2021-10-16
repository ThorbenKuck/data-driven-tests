package com.github.thorbenkuck.ddt.descriptors;

import com.github.thorbenkuck.ddt.execution.LifeCycle;
import com.github.thorbenkuck.ddt.execution.context.ConfigurableExecutionContext;
import com.github.thorbenkuck.ddt.selectors.Identification;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestClassDescriptor extends AbstractTestDescriptor implements DataDrivenDescriptor {

    private final Class<?> root;
    private final List<Method> methods;
    private int started = 0;

    private TestClassDescriptor(UniqueId engineId, Class<?> root, List<Method> methods) {
        super(engineId.append("class", root.getName()), root.getSimpleName(), ClassSource.from(root));
        this.root = root;
        this.methods = methods;
    }

    public static TestClassDescriptor build(UniqueId engineId, Class<?> root, List<Method> methods) {
        if(methods.isEmpty()) {
            throw new IllegalArgumentException("A TestClassDescriptor requires at minimum one method to execute");
        }
        return new TestClassDescriptor(engineId, root, methods).resolve();
    }

    public static TestClassDescriptor build(UniqueId engineId, Class<?> root) {
        return build(engineId, root, Arrays.stream(root.getMethods())
                        .filter(Identification.isTestScenario())
                        .collect(Collectors.toList()));
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    public TestClassDescriptor resolve() {
        methods.stream()
                .map(method -> new TestMethodDescriptor(this, root, method))
                .forEach(testMethodDescriptor -> addChild(testMethodDescriptor.resolve()));

        return this;
    }

    @Override
    public void started() {
        LifeCycle.executeBeforeAll(root);
    }

    public void started(ConfigurableExecutionContext context) {
        if (started == 0) {
            context.getTestExecutionState().testClassReached();
            started++;
        }
    }

    public void ended(ConfigurableExecutionContext context) {
        started--;
        if (started == 0) {
            context.getTestExecutionState().testClassReached();
        }
    }

    @Override
    public void ended() {
        LifeCycle.executeAfterAll(root);
    }
}
