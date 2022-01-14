package com.github.thorbenkuck.ddt.reflections;

import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.function.Function;

public class AnnotationMappingStage<A extends Annotation, T> extends AnnotationCollectorStage<T, AnnotationMappingStage<A, T>> {

    private final OrderedSet<AnnotatedElement> annotatedElements;

    public AnnotationMappingStage(OrderedSet<T> mappedValues, OrderedSet<AnnotatedElement> annotatedElements) {
        super(mappedValues);
        this.annotatedElements = annotatedElements;
    }

    public <S> AnnotationMappingStage<A, S> map(Function<T, S> function) {
        OrderedSet<S> mappedValues = values.stream()
                .map(function)
                .collect(OrderedSet.collector());

        return new AnnotationMappingStage<>(mappedValues, annotatedElements);
    }

    public <S> AnnotationMappingStage<A, S> mapNotNull(Function<T, S> function) {
        OrderedSet<S> mappedValues = OrderedSet.create();

        for (T value : values) {
            S mappedValue = function.apply(value);
            if(mappedValue != null) {
                mappedValues.add(mappedValue);
            }
        }

        return new AnnotationMappingStage<>(mappedValues, annotatedElements);
    }

    public <S> AnnotationMappingStage<A, S> flatMap(Function<T, ? extends S[]> function) {
        OrderedSet<S> mappedValues = values.stream()
                .flatMap(it -> Arrays.stream(function.apply(it)))
                .collect(OrderedSet.collector());

        return new AnnotationMappingStage<>(mappedValues, annotatedElements);
    }

    public <S extends Annotation> AnnotationMappingStage<A, T> thenAppendAllFor(Class<S> annotation, Function<S, T> function) {
        AnnotationCollector.of(annotation)
                .searchIn(annotatedElements)
                .analyze()
                .map(function)
                .applyTo(values);
        return this;
    }

    public <S extends Annotation> AnnotationMappingStage<A, T> flatAppendAllFor(
            Class<S> annotation,
            Function<S, T[]> function
    ) {
        values.addAll(
                AnnotationCollector.of(annotation)
                        .searchIn(annotatedElements)
                        .analyze()
                        .flatMap(function)
                        .asList()
        );
        return this;
    }

    public AnnotationMappingStage<A, T> prepend(T element) {
        OrderedSet<T> of = OrderedSet.of(element);
        of.addAll(values);
        values.set(of);
        of.clear();

        return this;
    }

    @Override
    protected AnnotationMappingStage<A, T> getInstance() {
        return this;
    }
}
