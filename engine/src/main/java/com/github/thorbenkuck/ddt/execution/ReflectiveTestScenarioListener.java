package com.github.thorbenkuck.ddt.execution;

import com.github.thorbenkuck.ddt.api.domain.TestScenarioListener;
import com.github.thorbenkuck.ddt.execution.context.ConfigurableExecutionContext;
import com.github.thorbenkuck.ddt.api.annotations.lifecycle.AfterScenario;
import com.github.thorbenkuck.ddt.api.annotations.lifecycle.BeforeScenario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.engine.TestExecutionResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectiveTestScenarioListener implements TestScenarioListener {

    private final List<Method> beforeScenarioMethods;
    private final List<Method> beforeEachMethods;
    private final List<Method> afterEachMethods;
    private final List<Method> afterScenarioMethods;
    private final ConfigurableExecutionContext context;

    public ReflectiveTestScenarioListener(ConfigurableExecutionContext context) {
        Class<?> type = context.getTestClass();
        this.context = context;
        beforeScenarioMethods = Arrays.stream(type.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(BeforeScenario.class))
                .filter(it -> it.getParameterCount() == 0)
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
        beforeEachMethods = Arrays.stream(type.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(BeforeEach.class))
                .filter(it -> it.getParameterCount() == 0)
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
        afterEachMethods = Arrays.stream(type.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(AfterEach.class))
                .filter(it -> it.getParameterCount() == 0)
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
        afterScenarioMethods = Arrays.stream(type.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(AfterScenario.class))
                .filter(it -> it.getParameterCount() == 0)
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
    }

    @Override
    public void beforeMethod() {
        if(!beforeScenarioMethods.isEmpty()) {
            for (Method beforeAllMethod : beforeScenarioMethods) {
                try {
                    beforeAllMethod.invoke(context.requireTestClassInstance());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    @Override
    public void beforeCase(Object testInstance) {
        if(!beforeEachMethods.isEmpty()) {
            for (Method method : beforeEachMethods) {
                try {
                    method.invoke(testInstance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    @Override
    public void afterCase(Object testInstance, TestExecutionResult result) {
        if(!afterEachMethods.isEmpty()) {
            for (Method method : afterEachMethods) {
                try {
                    method.invoke(testInstance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    @Override
    public void afterMethod() {
        if(!afterScenarioMethods.isEmpty()) {
            for (Method method : afterScenarioMethods) {
                try {
                    method.invoke(context.requireTestClassInstance());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }
}
