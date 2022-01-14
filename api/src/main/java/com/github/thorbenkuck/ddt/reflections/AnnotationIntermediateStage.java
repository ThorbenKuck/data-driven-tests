package com.github.thorbenkuck.ddt.reflections;

import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.function.Function;

public class AnnotationIntermediateStage<T extends Annotation> extends AnnotationCollectorStage<T, AnnotationIntermediateStage<T>> {

    private final OrderedSet<AnnotatedElement> annotatedElements;

    public AnnotationIntermediateStage(OrderedSet<T> annotations, OrderedSet<AnnotatedElement> annotatedElements) {
        super(annotations);
        this.annotatedElements = annotatedElements;
    }

    public <S> AnnotationMappingStage<T, S> map(Function<T, S> function) {
        OrderedSet<S> mappedValues = values.stream()
                .map(function)
                .collect(OrderedSet.collector());

        return new AnnotationMappingStage<>(mappedValues, annotatedElements);
    }

    public <S> AnnotationMappingStage<T, S> flatMap(Function<T, ? extends S[]> function) {
        OrderedSet<S> mappedValues = values.stream()
                .flatMap(it -> Arrays.stream(function.apply(it)))
                .collect(OrderedSet.collector());

        return new AnnotationMappingStage<>(mappedValues, annotatedElements);
    }

    @Override
    protected AnnotationIntermediateStage<T> getInstance() {
        return this;
    }
}
