package com.github.thorbenkuck.ddt;

import com.github.thorbenkuck.ddt.api.annotations.ConsumesAnnotation;
import org.junit.platform.commons.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Reflections {

    public static <T> T newInstance(Class<T> clazz, Object... args) {
        return ReflectionUtils.newInstance(clazz, args);
    }

    public static <T> T newInstance(Class<T> clazz, Class<?> testScenario, Method testMethod, Object... args) {
        T t = newInstance(clazz, args);

        consumeInheritedAnnotations(t, testScenario, testMethod);

        return t;
    }

    public static <T> void consumeInheritedAnnotations(T t, Class<?> testScenario, Method testMethod) {
        Arrays.stream(t.getClass()
                .getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(ConsumesAnnotation.class))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> method.getParameterTypes()[0].isAnnotation())
                .forEach(method -> {
                    Class<? extends Annotation> annotationType = (Class<? extends Annotation>) method.getParameterTypes()[0];
                    Set<Annotation> annotations = new HashSet<>(Annotations.deepFindIn(testMethod, annotationType));
                    annotations.addAll(Annotations.deepFindIn(testScenario, annotationType));
                    annotations.stream()
                            .findFirst()
                            .ifPresent(annotation -> {
                        try {
                            method.setAccessible(true);
                            method.invoke(t, annotation);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new IllegalStateException(e);
                        }
                    });
                });
    }
}
