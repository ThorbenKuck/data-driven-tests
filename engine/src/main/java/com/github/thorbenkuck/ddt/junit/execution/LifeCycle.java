package com.github.thorbenkuck.ddt.junit.execution;

import com.github.thorbenkuck.ddt.api.annotations.marker.BeforeCase;
import com.github.thorbenkuck.ddt.api.annotations.marker.AfterCase;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
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
        Arrays.stream(type.getDeclaredMethods())
                .filter(it -> Modifier.isStatic(it.getModifiers()))
                .filter(it -> AnnotationCollector.anyMatch(annotation, it))
                .forEach(method -> {
                    try {
                        method.invoke(null);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                });

    }

    public static void invokeNonStaticMethodWithAnnotation(Object target, Class<? extends Annotation> annotation) {
        Arrays.stream(target.getClass().getDeclaredMethods())
                .filter(it -> !Modifier.isStatic(it.getModifiers()))
                .filter(it -> AnnotationCollector.anyMatch(annotation, it))
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
        invokeNonStaticMethodWithAnnotation(o, BeforeCase.class);
    }

    public static void executeAfterScenario(Object o) {
        invokeNonStaticMethodWithAnnotation(o, AfterCase.class);
    }
}
