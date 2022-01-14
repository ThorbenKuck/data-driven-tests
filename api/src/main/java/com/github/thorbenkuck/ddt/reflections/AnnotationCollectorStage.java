package com.github.thorbenkuck.ddt.reflections;

import com.github.thorbenkuck.ddt.api.domain.listener.ContextFreeScenarioListener;
import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.util.*;
import java.util.function.Function;

public abstract class AnnotationCollectorStage<T, SELF> {

    protected final OrderedSet<T> values;

    protected AnnotationCollectorStage(OrderedSet<T> values) {
        this.values = values;
    }

    public void cleanup() {
        values.clear();
    }

    public List<T> asList() {
        return values.list();
    }

    public T[] asArray(T[] array) {
        return asList().toArray(array);
    }

    public Optional<T> getFirst() {
        return values.first();
    }

    public <S> S sinkInto(Function<OrderedSet<T>, S> function) {
        return function.apply(values);
    }

    public void sinkInto(List<T> list) {
        list.addAll(values);
        values.clear();
    }

    public OrderedSet<T> asOrderedSet() {
        return values.copy();
    }

    public boolean noMatch() {
        return values.isEmpty();
    }

    public boolean anyMatch() {
        return !noMatch();
    }

    public SELF prepend(T t) {
        return prependAll(Collections.singletonList(t));
    }

    public SELF prependAll(T[] t) {
        return prependAll(Arrays.asList(t));
    }
    public SELF prependAll(Collection<T> t) {
        OrderedSet<T> temp = OrderedSet.of(t);
        temp.addAll(values);
        values.set(temp);
        temp.clear();

        return getInstance();
    }

    public SELF append(T t) {
        values.add(t);

        return getInstance();
    }

    public SELF appendAll(T[] array) {
        return appendAll(array);
    }
    public SELF appendAll(Collection<T> collection) {
        values.addAll(collection);

        return getInstance();
    }
    public void applyTo(Collection<T> collection) {
        collection.addAll(values);
    }

    protected abstract SELF getInstance();
}
