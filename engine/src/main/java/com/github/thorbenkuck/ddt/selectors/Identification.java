package com.github.thorbenkuck.ddt.selectors;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;

public class Identification {
    public static final Predicate<Method> IS_DATA_DRIVEN_TEST_METHOD = method -> {
        if (!AnnotationSupport.isAnnotated(method, TestScenario.class)) {
            return false;
        }
        if (ReflectionUtils.isAbstract(method)) {
            return false;
        }
        if (ReflectionUtils.isPrivate(method)) {
            return false;
        }
        if (ReflectionUtils.isStatic(method)) {
            return false;
        }
        if (method.getParameterCount() == 0 || method.getParameterCount() > 2) {
            return false;
        }
        if (method.getReturnType().equals(Void.TYPE)) {
            return method.getParameterCount() == 2;
        } else {
            return method.getParameterCount() == 1;
        }
    };

    public static final Predicate<Class<?>> IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD = clazz -> {
        if (ReflectionUtils.isAbstract(clazz)) {
            return false;
        }
        if (ReflectionUtils.isPrivate(clazz)) {
            return false;
        }
        if (ReflectionUtils.isStatic(clazz)) {
            return false;
        }

        return Arrays.stream(clazz.getMethods())
                .anyMatch(IS_DATA_DRIVEN_TEST_METHOD);
    };

    public static Predicate<Class<?>> containsTestScenario() {
        return IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD;
    }

    public static Predicate<Method> isTestScenario() {
        return IS_DATA_DRIVEN_TEST_METHOD;
    }

    public static boolean containsTestScenario(Class<?> type) {
        return IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD.test(type);
    }

    public static boolean isTestScenario(Method method) {
        return IS_DATA_DRIVEN_TEST_METHOD.test(method);
    }
}
