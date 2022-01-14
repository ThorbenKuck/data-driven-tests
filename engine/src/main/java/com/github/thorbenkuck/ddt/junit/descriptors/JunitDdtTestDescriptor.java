package com.github.thorbenkuck.ddt.junit.descriptors;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.DdtDescriptor;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface JunitDdtTestDescriptor extends TestDescriptor {

    TestResult execute(ExecutionRequest request);

    DdtDescriptor descriptor();

    default TestResult executeInContext(ExecutionRequest request, Supplier<TestResult> executionResultSupplier) {
        try {
            request.getEngineExecutionListener().executionStarted(this);

            descriptor().started();
            TestResult result = executionResultSupplier.get();
            descriptor().ended(result);

            request.getEngineExecutionListener().executionFinished(this, Results.map(result));
            return result;
        } catch (Throwable e) {
            TestResult result = TestResult.of(e);
            descriptor().ended(result);
            request.getEngineExecutionListener().executionFinished(this, Results.map(result));
            return result;
        }
    }

    default TestResult executeAsContainer(ExecutionRequest request) {
        return executeInContext(request, () -> {
            request.getEngineExecutionListener().executionStarted(this);
            TestResult result = TestResult.successful();
            List<JunitDdtTestDescriptor> collect = getChildren()
                    .stream()
                    .filter(it -> it instanceof JunitDdtTestDescriptor)
                    .map(it -> (JunitDdtTestDescriptor) it)
                    .collect(Collectors.toList());

            for (JunitDdtTestDescriptor junitDdtTestDescriptor : collect) {
                try {
                    result = result.and(junitDdtTestDescriptor.execute(request));
                } catch (Throwable e) {
                    result = result.and(TestResult.of(e));
                }
            }
            request.getEngineExecutionListener().executionFinished(this, Results.map(result.compact()));

            return result;
        });
    }

}
