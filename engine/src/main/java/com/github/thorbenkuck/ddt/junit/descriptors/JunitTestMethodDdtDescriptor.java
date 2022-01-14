package com.github.thorbenkuck.ddt.junit.descriptors;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.DdtDescriptor;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestMethodDescriptor;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

public class JunitTestMethodDdtDescriptor extends AbstractTestDescriptor implements JunitDdtTestDescriptor {

    private final TestMethodDescriptor descriptor;

    public JunitTestMethodDdtDescriptor(UniqueId classId, TestMethodDescriptor descriptor) {
        super(
                uniqueId(descriptor, classId),
                descriptor.suggestedDisplayName(),
                MethodSource.from(descriptor.getParent().testClass(), descriptor.testMethod())
        );
        this.descriptor = descriptor;
        resolveChildren();
    }

    private void resolveChildren() {
        descriptor.getChildren()
                .stream()
                .map(testCaseDescriptor -> new JunitTestCaseDdtDescriptor(getUniqueId(), testCaseDescriptor))
                .forEach(this::addChild);
    }

    private static UniqueId uniqueId(TestMethodDescriptor testClassDescriptor, UniqueId classId) {
        return classId.append("method", testClassDescriptor.testMethod().getName());
    }

    @Override
    public Type getType() {
        return Type.CONTAINER_AND_TEST;
    }

    @Override
    public TestResult execute(ExecutionRequest request) {
        return executeAsContainer(request);
    }

    @Override
    public DdtDescriptor descriptor() {
        return descriptor;
    }

    public TestMethodDescriptor typedParent() {
        return descriptor;
    }
}
