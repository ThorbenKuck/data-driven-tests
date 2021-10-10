package com.github.thorbenkuck.ddt.descriptors;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.Annotations;
import com.github.thorbenkuck.ddt.execution.LifeCycle;
import com.github.thorbenkuck.ddt.execution.context.ConfigurableExecutionContext;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.util.Arrays;

public class TestClassDescriptor extends AbstractTestDescriptor implements DataDrivenDescriptor {

    private final Class<?> root;
    private int started = 0;

    public TestClassDescriptor(UniqueId engineId, Class<?> root) {
        super(engineId.append("class", root.getName()), root.getSimpleName(), ClassSource.from(root));
        this.root = root;
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    public TestClassDescriptor resolve() {
        Arrays.stream(root.getMethods())
                .filter(method -> !Annotations.deepFindIn(method, TestScenario.class).isEmpty())
                .map(method -> new TestMethodDescriptor(this, root, method))
                .forEach(testMethodDescriptor -> addChild(testMethodDescriptor.resolve()));

        return this;
    }

    @Override
    public void started() {
        LifeCycle.executeBeforeAll(root);
    }

    public void started(ConfigurableExecutionContext context) {
        if(started == 0) {
            context.getTestExecutionState().testClassReached();
            started++;
        }
    }

    public void ended(ConfigurableExecutionContext context) {
        started--;
        if(started == 0) {
            context.getTestExecutionState().testClassReached();
        }
    }

    @Override
    public void ended() {
        LifeCycle.executeAfterAll(root);
    }
}
