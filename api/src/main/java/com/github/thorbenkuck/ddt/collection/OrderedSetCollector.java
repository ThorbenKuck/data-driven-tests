package com.github.thorbenkuck.ddt.collection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class OrderedSetCollector<T> implements Collector<T, OrderedSet<T>, OrderedSet<T>> {
    @Override
    public Supplier<OrderedSet<T>> supplier() {
        return OrderedSet::create;
    }

    @Override
    public BiConsumer<OrderedSet<T>, T> accumulator() {
        return OrderedSet::add;
    }

    @Override
    public BinaryOperator<OrderedSet<T>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<OrderedSet<T>, OrderedSet<T>> finisher() {
        return it -> it;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>(Collections.singletonList(Characteristics.IDENTITY_FINISH));
    }
}
