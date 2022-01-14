package com.github.thorbenkuck.ddt.api.annotations.importer.meta;

import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import com.github.thorbenkuck.ddt.api.annotations.marker.ConsumesAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConsumesMethodMetaData {

    private final Method method;
    private final Annotation annotation;

    public ConsumesMethodMetaData(Method method, Annotation annotation) {
        this.method = method;
        this.annotation = annotation;
    }

    public static ConsumesMethodMetaData of(Method method) {
        return AnnotationCollector.of(ConsumesAnnotation.class)
                .findFirst(method)
                .map(value -> new ConsumesMethodMetaData(method, value))
                .orElseGet(() -> new ConsumesMethodMetaData(null, null));
    }

    public boolean isValid() {
        return method != null && annotation != null;
    }

    public static List<ConsumesMethodMetaData> extractValidOf(Class<?>... classes) {
        return Arrays.stream(classes)
                .flatMap(clazz -> Arrays.stream(clazz.getMethods())
                        .map(ConsumesMethodMetaData::of)
                )
                .filter(ConsumesMethodMetaData::isValid)
                .collect(Collectors.toList());
    }

    public void invoke(Object t) {
        if(method.getParameterCount() != 1) {
            return;
        }
        if(!method.getParameterTypes()[0].equals(annotation.getClass())) {
            return;
        }
        try {
            method.setAccessible(true);
            method.invoke(t, annotation);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}