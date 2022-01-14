package com.github.thorbenkuck.ddt.junit.descriptors;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.DdtDescriptor;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestClassDescriptor;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.ClassSource;

public class JunitTestClassDdtDescriptor extends AbstractTestDescriptor implements JunitDdtTestDescriptor {

    private final TestClassDescriptor descriptor;

    public JunitTestClassDdtDescriptor(UniqueId engineId, TestClassDescriptor descriptor) {
        super(
                uniqueId(engineId, descriptor),
                descriptor.suggestedDisplayName(),
                ClassSource.from(descriptor.testClass())
        );
        this.descriptor = descriptor;
        resolveChildren();
    }

    private static UniqueId uniqueId(UniqueId engineId, TestClassDescriptor descriptor) {
        return engineId.append("class", descriptor.testClass().getName());
    }

    private void resolveChildren() {
        descriptor.getChildren()
                .stream()
                .map(testMethodDescriptor -> new JunitTestMethodDdtDescriptor(getUniqueId(), testMethodDescriptor))
                .forEach(this::addChild);
    }

    @Override
    public Type getType() {
        return Type.CONTAINER;
    }

    @Override
    public TestResult execute(ExecutionRequest request) {
        return executeAsContainer(request);
    }

    @Override
    public DdtDescriptor descriptor() {
        return descriptor;
    }

    public TestClassDescriptor typedParent() {
        return descriptor;
    }
}
