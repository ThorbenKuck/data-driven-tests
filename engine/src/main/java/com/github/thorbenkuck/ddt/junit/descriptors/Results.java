package com.github.thorbenkuck.ddt.junit.descriptors;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import org.junit.platform.engine.TestExecutionResult;

public class Results {

    public static TestExecutionResult map(TestResult testResult) {
        switch (testResult.getStatus()) {
            case SUCCESSFUL: return TestExecutionResult.successful();
            case FAILED: return TestExecutionResult.failed(testResult.getThrowable().orElse(null));
            case ABORTED: return TestExecutionResult.aborted(testResult.getThrowable().orElse(null));
        }

        throw new UnsupportedOperationException("Unmapped status " + testResult);
    }

}
