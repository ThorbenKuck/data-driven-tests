package com.github.thorbenkuck.ddt.junit.descriptors;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.DdtDescriptor;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestCaseDescriptor;
import org.junit.platform.engine.*;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.descriptor.MethodSource;

public class JunitTestCaseDdtDescriptor extends AbstractTestDescriptor implements JunitDdtTestDescriptor {

    private final TestCaseDescriptor descriptor;

    protected JunitTestCaseDdtDescriptor(
            UniqueId methodId,
            TestCaseDescriptor descriptor
    ) {
        super(
                uniqueId(methodId, descriptor),
                descriptor.suggestedDisplayName(),
                testSource(descriptor)
        );
        this.descriptor = descriptor;
    }

    private static UniqueId uniqueId(UniqueId methodId, TestCaseDescriptor testCaseDescriptor) {
        return methodId.append("case", testCaseDescriptor.suggestedDisplayName());
    }

    private static TestSource testSource(TestCaseDescriptor testCaseDescriptor) {
        return MethodSource.from(testCaseDescriptor.getParent().testMethod());
    }

    @Override
    public TestDescriptor.Type getType() {
        return Type.TEST;
    }

    @Override
    public TestResult execute(ExecutionRequest request) {
        return executeInContext(request, descriptor::run);
    }

    @Override
    public DdtDescriptor descriptor() {
        return descriptor;
    }
}
