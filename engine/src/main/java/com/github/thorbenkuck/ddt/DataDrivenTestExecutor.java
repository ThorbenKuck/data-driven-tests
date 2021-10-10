package com.github.thorbenkuck.ddt;

import com.github.thorbenkuck.ddt.descriptors.TestCaseDescriptor;
import com.github.thorbenkuck.ddt.descriptors.DataDrivenDescriptor;
import com.github.thorbenkuck.ddt.execution.context.ConfigurableExecutionContext;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;

public class DataDrivenTestExecutor {

    public void execute(ExecutionRequest request, TestDescriptor descriptor) {
        if(descriptor instanceof TestCaseDescriptor) {
            executeTestCases(request, (TestCaseDescriptor) descriptor);
        } else if (descriptor instanceof DataDrivenDescriptor) {
            executeDataDrivenContainer(request, (DataDrivenDescriptor) descriptor);
        } else {
            executeContainer(request, descriptor);
        }
    }

    private void executeTestCases(ExecutionRequest request, TestCaseDescriptor testCaseDescriptor) {
        request.getEngineExecutionListener().executionStarted(testCaseDescriptor);
        testCaseDescriptor.started();

        ConfigurableExecutionContext context = testCaseDescriptor.getContext();
        TestExecutionResult executionResult = context.getTestExecutionState().executeTest(context, testCaseDescriptor.getDisplayName());

        testCaseDescriptor.ended();
        request.getEngineExecutionListener().executionFinished(testCaseDescriptor, executionResult);
    }

    private void executeDataDrivenContainer(ExecutionRequest request, DataDrivenDescriptor containerDescriptor) {
        request.getEngineExecutionListener().executionStarted(containerDescriptor);
        containerDescriptor.started();

        for (TestDescriptor descriptor : containerDescriptor.getChildren()) {
            execute(request, descriptor);
        }

        containerDescriptor.ended();
        request.getEngineExecutionListener().executionFinished(containerDescriptor, TestExecutionResult.successful());
    }

    private void executeContainer(ExecutionRequest request, TestDescriptor containerDescriptor) {
        request.getEngineExecutionListener().executionStarted(containerDescriptor);
        for (TestDescriptor descriptor : containerDescriptor.getChildren()) {
            execute(request, descriptor);
        }
        request.getEngineExecutionListener().executionFinished(containerDescriptor, TestExecutionResult.successful());
    }
}
