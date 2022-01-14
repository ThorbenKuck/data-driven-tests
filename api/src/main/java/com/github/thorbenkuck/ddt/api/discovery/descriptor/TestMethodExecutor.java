package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import com.github.thorbenkuck.ddt.api.annotations.marker.ApplyPreconditions;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.domain.MessageContext;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.api.registry.PluginDiscoverer;
import com.github.thorbenkuck.ddt.api.registry.TypeConverterAdapterRegistry;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.properties.DdtProperties;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import org.junit.platform.commons.util.ReflectionUtils;
import org.opentest4j.AssertionFailedError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class TestMethodExecutor {

    private final ConfigurableTestContext context;
    private final TypeConverterAdapterRegistry typeConverterAdapters;

    public TestMethodExecutor(ConfigurableTestContext context) {
        this.context = context;
        typeConverterAdapters = PluginDiscoverer.loadTypeConverterAdapter(context.getEngineCore());
    }

    public TestResult invokeTestMethod(String displayName) {
        final TestResult result;
        final Method method = context.requireTestMethod();

        if (method.getParameterCount() == 1) {
            result = executeWithSingleInputParameter(
                    method,
                    method.getParameterTypes()[0],
                    displayName,
                    typeConverterAdapters
            );
        } else {
            result = executeWithDoubleParameter(
                    method,
                    method.getParameterTypes()[0],
                    method.getParameterTypes()[1],
                    displayName,
                    typeConverterAdapters
            );
        }
        return result;
    }

    private void fill(MessageContext errorMessageContext, Method method, String displayName, Object testInstance) {
        errorMessageContext.addVariable("method", method.getName());
        errorMessageContext.addVariable("case", displayName);
        errorMessageContext.addVariable("class", testInstance.getClass().getName());
    }

    private TestResult executeWithSingleInputParameter(
            Method method,
            Class<?> parameterType,
            String displayName,
            TypeConverterAdapterRegistry typeConverterAdapters
    ) {
        final Object parameter;
        final Object expectedResult;
        final Object actualResult;
        final TestCaseContent testCaseContent = context.requireTestCaseContent();
        final Object testInstance = context.requireTestInstance();
        final OrderedSet<Asserter> asserters = context.getAsserters();

        // %method%.%case% [%asserter%] %message% %ls%
        final MessageContext errorMessageContext = new MessageContext(DdtProperties.caseErrorMessage());
        fill(errorMessageContext, method, displayName, testInstance);

        try {
            parameter = testCaseContent.convertInputTo(parameterType, typeConverterAdapters);
        } catch (Throwable throwable) {
            return TestResult.aborted(new IllegalStateException("Construction of input data failed", throwable));
        }
        try {
            expectedResult = testCaseContent.convertOutputTo(method.getReturnType(), typeConverterAdapters);
        } catch (Throwable throwable) {
            return TestResult.aborted(new IllegalStateException("Construction of the expected result failed", throwable));
        }

        try {
            actualResult = ReflectionUtils.invokeMethod(method, testInstance, parameter);
        } catch (Throwable throwable) {
            String message = String.format(
                    "Test '%s' failed for instance '%s'",
                    displayName,
                    testInstance
            );
            return TestResult.failed(new AssertionFailedError(message, throwable));
        }

        List<String> errors = new ArrayList<>();

        asserters.forEach(asserter -> {
            try {
                asserter.doAssert(expectedResult, actualResult);
            } catch (Throwable assertionError) {
                String errorMessage = errorMessageContext.build(
                        Map.of(
                                "asserter", asserter.name(),
                                "message", assertionError.getMessage()
                        )
                );
                errors.add(errorMessage);
            }
        });

        if (errors.isEmpty()) {
            return TestResult.successful();
        } else {
            String message = System.lineSeparator() + String.join(System.lineSeparator(), errors);
            return TestResult.failed(new AssertionFailedError(message, expectedResult, actualResult));
        }
    }

    private TestResult executeWithDoubleParameter(
            Method method,
            Class<?> inputType,
            Class<?> resultType,
            String displayName,
            TypeConverterAdapterRegistry typeConverterAdapters
    ) {
        final Object inputParameter;
        final Object expectedResult;
        final TestCaseContent testCaseContent = context.requireTestCaseContent();
        final Object testInstance = context.requireTestInstance();
        final MessageContext errorMessageContext = new MessageContext(DdtProperties.testErrorMessage());
        fill(errorMessageContext, method, displayName, testInstance);

        try {
            inputParameter = testCaseContent.convertInputTo(inputType, typeConverterAdapters);
        } catch (Throwable throwable) {
            return TestResult.aborted(new IllegalStateException("Construction of input data failed", throwable));
        }
        try {
            expectedResult = testCaseContent.convertOutputTo(resultType, typeConverterAdapters);
        } catch (Throwable throwable) {
            return TestResult.aborted(new IllegalStateException("Construction of the expected result failed", throwable));
        }

        try {
            ReflectionUtils.invokeMethod(method, testInstance, inputParameter, expectedResult);
            return TestResult.successful();
        } catch (Throwable throwable) {
            String errorMessage = errorMessageContext.build(
                    Map.of(
                            "message", throwable.getMessage()
                    )
            );
            return TestResult.failed(new AssertionFailedError(errorMessage, throwable));
        }
    }

    /**
     * TODO: Make me beautiful
     */
    public void applyPreconditions() {
        Arrays.stream(context.getTestClass().getMethods())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .filter(method -> AnnotationCollector.anyMatch(ApplyPreconditions.class, method))
                .filter(method -> method.getReturnType().equals(Void.TYPE))
                .forEach(method -> {
                    context.getTestCaseContent().ifPresent(content -> {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Object[] parameters = new Object[parameterTypes.length];

                        for (int i = 0; i < parameters.length; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            Optional<Object> first = content.preconditions()
                                    .stream()
                                    .filter(it -> it.getClass().equals(parameterType))
                                    .findFirst();
                            if(!first.isPresent() && !DdtProperties.failOnMissingPrecondition()) {
                                return;
                            }

                            parameters[i] = first.orElseThrow(
                                    () -> new IllegalArgumentException("No Precondition of type " + parameterType + " present in the provided preconditions")
                            );
                        }

                        final boolean canAccess = method.canAccess(context.requireTestInstance());
                        try {
                            method.setAccessible(true);
                            content.preconditions();
                            method.invoke(context.requireTestInstance(), parameters);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IllegalStateException(e);
                        } finally {
                            method.setAccessible(canAccess);
                        }
                    });
                });
    }
}
