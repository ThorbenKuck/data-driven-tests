package com.github.thorbenkuck.ddt.reflections.analyzer;

import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.annotation.Annotation;

public class ClassAnalyzer implements Analyzer<Class<?>> {

    @Override
    public <T extends Annotation> OrderedSet<T> findIn(Class<T> annotationType, Class<?> aClass) {
        if(aClass.isAnnotationPresent(annotationType)) {
            return OrderedSet.of(aClass.getAnnotation(annotationType));
        }

        return declaredAnnotation.findIn(annotationType, aClass);
    }
}
