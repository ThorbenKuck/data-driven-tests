package com.github.thorbenkuck.ddt.reflections.analyzer;

import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

public interface Analyzer<S> {

    <T extends Annotation> OrderedSet<T> findIn(Class<T> annotationType, S s);

    default <T extends Annotation> OrderedSet<T> findIn(Class<T> annotationType, Collection<S> collection) {
        OrderedSet<T> result = OrderedSet.create();

        collection.forEach(instance -> findIn(annotationType, instance).sinkInto(result));

        return result;
    }

    default <T extends Annotation> OrderedSet<T> findIn(Class<T> annotationType, S[] array) {
        OrderedSet<T> result = OrderedSet.create();

        Arrays.stream(array).forEach(instance -> findIn(annotationType, instance).sinkInto(result));

        return result;
    }

    ClassAnalyzer clazz = new ClassAnalyzer();

    DeclaredAnnotationAnalyzer declaredAnnotation = new DeclaredAnnotationAnalyzer();

    AnnotationAnalyzer annotation = new AnnotationAnalyzer();

    MethodAnalyzer method = new MethodAnalyzer();

    TypeDeterminationAnalyzer auto = new TypeDeterminationAnalyzer();

}
