package com.github.thorbenkuck.ddt.junit;

import com.github.thorbenkuck.ddt.junit.descriptors.JunitDdtTestDescriptor;
import com.github.thorbenkuck.ddt.junit.descriptors.JunitTestClassDdtDescriptor;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;

public class DataDrivenTestExecutor {

    public void execute(ExecutionRequest request, TestDescriptor descriptor) {
        if (descriptor instanceof JunitDdtTestDescriptor) {
            ((JunitTestClassDdtDescriptor) descriptor).execute(request);
        } else {
            executeContainer(request, descriptor);
        }
    }

    private void executeContainer(ExecutionRequest request, TestDescriptor containerDescriptor) {
        request.getEngineExecutionListener().executionStarted(containerDescriptor);
        for (TestDescriptor descriptor : containerDescriptor.getChildren()) {
            execute(request, descriptor);
        }
        request.getEngineExecutionListener().executionFinished(containerDescriptor, TestExecutionResult.successful());
    }
}
