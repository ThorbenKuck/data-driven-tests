package com.github.thorbenkuck.ddt.api.domain.listener;

import com.github.thorbenkuck.ddt.api.annotations.marker.AfterCase;
import com.github.thorbenkuck.ddt.api.annotations.marker.BeforeCase;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.ExecutionContext;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectiveTestScenarioListener implements TestScenarioListener {

    private final List<Method> beforeCaseMethods;
    private final List<Method> beforeEachMethods;
    private final List<Method> afterEachMethods;
    private final List<Method> afterCaseMethods;

    public ReflectiveTestScenarioListener(Class<?> testClass) {
        beforeEachMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(BeforeEach.class))
                .filter(it -> it.getParameterCount() == 0)
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
        beforeCaseMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(BeforeCase.class))
                .filter(it -> it.getParameterCount() == 0)
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
        afterCaseMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(AfterCase.class))
                .filter(it -> it.getParameterCount() == 0)
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
        afterEachMethods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(AfterEach.class))
                .filter(it -> it.getParameterCount() == 0)
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .collect(Collectors.toList());
    }

    @Override
    public void beforeMethod(ExecutionContext context) {
        if(!beforeEachMethods.isEmpty()) {
            for (Method beforeAllMethod : beforeEachMethods) {
                try {
                    beforeAllMethod.invoke(context.requireTestInstance());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    @Override
    public void beforeCase(ExecutionContext context) {
        if(!beforeCaseMethods.isEmpty()) {
            for (Method method : beforeCaseMethods) {
                try {
                    method.invoke(context.requireTestInstance());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    @Override
    public void afterCase(ExecutionContext context, TestResult result) {
        if(!afterCaseMethods.isEmpty()) {
            for (Method method : afterCaseMethods) {
                try {
                    method.invoke(context.requireTestInstance());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    @Override
    public void afterMethod(ExecutionContext context, TestResult result) {
        if(!afterEachMethods.isEmpty()) {
            for (Method method : afterEachMethods) {
                try {
                    method.invoke(context.requireTestInstance());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }
}
