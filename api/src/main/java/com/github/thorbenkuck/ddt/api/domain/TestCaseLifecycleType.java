package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioLifecycle;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Method;
import java.util.Optional;

public enum TestCaseLifecycleType {

    PER_CLASS,
    PER_METHOD,
    PER_CASE;

    public static TestCaseLifecycleType valueOf(Class<?> clazz, Method method) {
        return valueOf(method).orElseGet(() ->
                valueOf(clazz).orElseGet(TestCaseLifecycleType::defaultValue));
    }

    public static Optional<TestCaseLifecycleType> valueOf(Class<?> clazz) {
        return Optional.ofNullable(clazz.getAnnotation(TestInstance.class))
                .map(annotation -> valueOf(annotation.value().name()));
    }

    public static Optional<TestCaseLifecycleType> valueOf(Method method) {
        return Optional.ofNullable(method.getAnnotation(ScenarioLifecycle.class))
                .map(annotation -> valueOf(annotation.value().name()));
    }

    public static TestCaseLifecycleType defaultValue() {
        return PER_METHOD;
    }
}
