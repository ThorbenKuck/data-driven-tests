package com.github.thorbenkuck.ddt.execution;

import com.github.thorbenkuck.ddt.api.annotations.lifecycle.BeforeScenario;
import com.github.thorbenkuck.ddt.api.annotations.lifecycle.AfterScenario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class LifeCycle {

    public static void invokeStaticMethodWithAnnotation(Class<?> type, Class<? extends Annotation> annotation) {
        Arrays.stream(type.getMethods())
                .filter(it -> Modifier.isStatic(it.getModifiers()))
                .filter(it -> it.isAnnotationPresent(annotation))
                .forEach(method -> {
                    try {
                        method.invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                });

    }

    public static void invokeNonStaticMethodWithAnnotation(Object target, Class<? extends Annotation> annotation) {
        Arrays.stream(target.getClass().getMethods())
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .filter(it -> it.isAnnotationPresent(annotation))
                .forEach(method -> {
                    try {
                        method.invoke(target);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                });

    }

    public static void executeBeforeAll(Class<?> type) {
        invokeStaticMethodWithAnnotation(type, BeforeAll.class);
    }

    public static void executeAfterAll(Class<?> type) {
        invokeStaticMethodWithAnnotation(type, AfterAll.class);
    }

    public static void executeBeforeEach(Object o) {
        invokeNonStaticMethodWithAnnotation(o, BeforeEach.class);
    }

    public static void executeAfterEach(Object o) {
        invokeNonStaticMethodWithAnnotation(o, AfterEach.class);
    }

    public static void executeBeforeScenario(Object o) {
        invokeNonStaticMethodWithAnnotation(o, BeforeScenario.class);
    }

    public static void executeAfterScenario(Object o) {
        invokeNonStaticMethodWithAnnotation(o, AfterScenario.class);
    }
}
