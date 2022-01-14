package com.github.thorbenkuck.ddt.reflections;

import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.reflections.analyzer.Analyzer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;

public class AnnotationCollector<T extends Annotation> {

    private final Class<T> annotationType;
    private final List<Supplier<OrderedSet<T>>> suppliers = new ArrayList<>();
    private final OrderedSet<AnnotatedElement> annotatedElements = OrderedSet.create();

    public AnnotationCollector(Class<T> annotationType) {
        this.annotationType = annotationType;
    }

    public AnnotationCollector<T> searchIn(Collection<AnnotatedElement> annotatedElements) {
        suppliers.add(() -> Analyzer.auto.findIn(annotationType, annotatedElements));
        this.annotatedElements.addAll(annotatedElements);
        return this;
    }

    public AnnotationCollector<T> searchIn(Method... methods) {
        suppliers.add(() -> Analyzer.method.findIn(annotationType, methods));
        annotatedElements.addAll(Arrays.asList(methods));
        return this;
    }

    public AnnotationCollector<T> searchIn(Class<?>... classes) {
        suppliers.add(() -> Analyzer.clazz.findIn(annotationType, classes));
        annotatedElements.addAll(Arrays.asList(classes));
        return this;
    }

    public AnnotationCollector<T> searchIn(Method method) {
        suppliers.add(() -> Analyzer.method.findIn(annotationType, method));
        annotatedElements.add(method);
        return this;
    }

    public AnnotationCollector<T> searchIn(Class<?> clazz) {
        suppliers.add(() -> Analyzer.clazz.findIn(annotationType, clazz));
        annotatedElements.add(clazz);
        return this;
    }

    public AnnotationIntermediateStage<T> searchOnlyIn(Collection<AnnotatedElement> annotatedElements) {
        suppliers.add(() -> Analyzer.auto.findIn(annotationType, annotatedElements));
        this.annotatedElements.addAll(annotatedElements);
        return analyze();
    }

    public AnnotationIntermediateStage<T> searchOnlyIn(Method... methods) {
        suppliers.add(() -> Analyzer.method.findIn(annotationType, methods));
        annotatedElements.addAll(Arrays.asList(methods));
        return analyze();
    }

    public AnnotationIntermediateStage<T> searchOnlyIn(Class<?>... classes) {
        suppliers.add(() -> Analyzer.clazz.findIn(annotationType, classes));
        annotatedElements.addAll(Arrays.asList(classes));
        return analyze();
    }

    public AnnotationIntermediateStage<T> searchOnlyIn(Method method) {
        suppliers.add(() -> Analyzer.method.findIn(annotationType, method));
        annotatedElements.add(method);
        return analyze();
    }

    public AnnotationIntermediateStage<T> searchOnlyIn(Class<?> clazz) {
        suppliers.add(() -> Analyzer.clazz.findIn(annotationType, clazz));
        annotatedElements.add(clazz);
        return analyze();
    }

    public T firstMatch(Method method) {
        return findFirst(method)
                .orElseThrow(() -> new IllegalArgumentException("Could not find the annotation " + annotationType + " in the method " + method));
    }

    public T firstMatch(Class<?> clazz) {
        return findFirst(clazz)
                .orElseThrow(() -> new IllegalArgumentException("Could not find the annotation " + annotationType + " in " + clazz));
    }

    public Optional<T> findFirst(Class<?> clazz) {
        return searchIn(clazz)
                .analyze()
                .getFirst();
    }

    public Optional<T> findFirst(Method method) {
        return searchIn(method)
                .analyze()
                .getFirst();
    }

    public AnnotationIntermediateStage<T> analyze() {
        OrderedSet<T> collect = suppliers.stream()
                .flatMap(it -> it.get().stream())
                .collect(OrderedSet.collector());
        suppliers.clear();

        return new AnnotationIntermediateStage<>(collect, annotatedElements);
    }

    public static <T extends Annotation> AnnotationCollector<T> of(Class<T> type) {
        return new AnnotationCollector<>(type);
    }

    public static <T extends Annotation> Optional<T> findFirst(Class<T> type, Method method) {
        return of(type).findFirst(method);
    }

    public static <T extends Annotation> Optional<T> findFirst(Class<T> type, Class<?> clazz) {
        return of(type).findFirst(clazz);
    }

    public static <T extends Annotation> T firstMatch(Class<T> type, Method method) {
        return of(type).firstMatch(method);
    }

    public static <T extends Annotation> T firstMatch(Class<T> type, Class<?> clazz) {
        return of(type).firstMatch(clazz);
    }

    public static <T extends Annotation> boolean anyMatch(Class<T> type, Method method) {
        return of(type).searchIn(method).analyze().anyMatch();
    }

    public static <T extends Annotation> boolean anyMatch(Class<T> type, Class<?> clazz) {
        return of(type).searchIn(clazz).analyze().anyMatch();
    }
}
