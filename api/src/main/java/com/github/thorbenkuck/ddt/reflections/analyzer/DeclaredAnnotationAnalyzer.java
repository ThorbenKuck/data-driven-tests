package com.github.thorbenkuck.ddt.reflections.analyzer;

import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;

public class DeclaredAnnotationAnalyzer implements Analyzer<AnnotatedElement> {

    @Override
    public <T extends Annotation> OrderedSet<T> findIn(Class<T> annotationType, AnnotatedElement annotatedElement) {
        return Arrays.stream(annotatedElement.getAnnotations())
                .flatMap(annotation -> Analyzer.annotation.findIn(annotationType, annotation).stream())
                .collect(OrderedSet.collector());
    }
}
