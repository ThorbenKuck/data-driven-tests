package com.github.thorbenkuck.ddt.execution;

import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.api.services.PluginDiscoverer;
import com.github.thorbenkuck.ddt.execution.context.ConfigurableExecutionContext;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.services.adapter.TypeConverterAdapterRegistry;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.TestExecutionResult;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.Method;
import java.util.*;

public class TestMethodExecutor {

    private final TypeConverterAdapterRegistry typeConverterAdapters = PluginDiscoverer.loadTypeConverterAdapter();

    public TestExecutionResult invokeTestMethod(ConfigurableExecutionContext executionContext, Object testInstance, String displayName) {
        TestExecutionResult result;
        Method method = executionContext.getTestMethod();
        if (method.getParameterCount() == 1) {
            result = executeWithSingleInputParameter(
                    method,
                    executionContext.requireCurrentContent(),
                    executionContext.requireTestClassInstance(),
                    method.getParameterTypes()[0],
                    executionContext.getAsserters(),
                    displayName
            );
        } else {
            result = executeWithDoubleParameter(
                    method,
                    executionContext.requireCurrentContent(),
                    executionContext.requireTestClassInstance(),
                    method.getParameterTypes()[0],
                    method.getParameterTypes()[1],
                    displayName
            );
        }
        return result;
    }

    private TestExecutionResult executeWithSingleInputParameter(
            Method testMethod,
            TestCaseContent testCaseContent,
            Object testInstance,
            Class<?> parameterType,
            List<Asserter> asserters,
            String displayName
    ) {
        final Object parameter;
        final Object expectedResult;
        final Object actualResult;

        try {
            parameter = testCaseContent.convertInputTo(parameterType, typeConverterAdapters);
        } catch (Throwable throwable) {
            return TestExecutionResult.aborted(new IllegalStateException("Construction of input data failed", throwable));
        }
        try {
            expectedResult = testCaseContent.convertOutputTo(testMethod.getReturnType(), typeConverterAdapters);
        } catch (Throwable throwable) {
            return TestExecutionResult.aborted(new IllegalStateException("Construction of the expected result failed", throwable));
        }

        try {
            actualResult = ReflectionUtils.invokeMethod(testMethod, testInstance, parameter);
        } catch (Throwable throwable) {
            String message = String.format( //
                    "Test '%s' failed for instance '%s'", //
                    displayName, //
                    testInstance.toString() //
            );
            return TestExecutionResult.failed(new AssertionFailedError(message, throwable));
        }

        List<String> errors = new ArrayList<>();

        asserters.forEach(asserter -> {
            try {
                asserter.doAssert(expectedResult, actualResult);
            } catch (Throwable assertionError) {
                errors.add("[" + asserter.name() + "]: " + assertionError.getMessage() + "\n");
            }
        });

        if (errors.isEmpty()) {
            return TestExecutionResult.successful();
        } else {
            String message = System.lineSeparator() + String.join(System.lineSeparator(), errors);
            return TestExecutionResult.failed(new AssertionFailedError(message, expectedResult, actualResult));
        }
    }

    private TestExecutionResult executeWithDoubleParameter(
            Method method,
            TestCaseContent testCaseContent,
            Object testInstance,
            Class<?> inputType,
            Class<?> resultType,
            String displayName
    ) {
        final Object inputParameter;
        final Object expectedResult;

        try {
            inputParameter = testCaseContent.convertInputTo(inputType, typeConverterAdapters);
        } catch (Throwable throwable) {
            return TestExecutionResult.aborted(new IllegalStateException("Construction of input data failed", throwable));
        }
        try {
            expectedResult = testCaseContent.convertOutputTo(resultType, typeConverterAdapters);
        } catch (Throwable throwable) {
            return TestExecutionResult.aborted(new IllegalStateException("Construction of the expected result failed", throwable));
        }

        try {
            ReflectionUtils.invokeMethod(method, testInstance, inputParameter, expectedResult);
            return TestExecutionResult.successful();
        } catch (Throwable throwable) {
            String message = String.format( //
                    "Test '%s' failed for instance '%s'", //
                    displayName, //
                    testInstance.toString() //
            );
            return TestExecutionResult.failed(new AssertionFailedError(message, throwable));
        }
    }
}
