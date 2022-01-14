package com.github.thorbenkuck.ddt.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collector;

public interface OrderedSet<T> extends Collection<T> {

    EmptyOrderedSet<?> EMPTY_ORDERED_SET = new EmptyOrderedSet<>();

    OrderedSetCollector<?> COLLECTOR = new OrderedSetCollector<>();

    static <T> OrderedSet<T> of(T element) {
        return new OrderedArraySet<>(Collections.singletonList(element));
    }

    static <T> OrderedSet<T> of(T... elements) {
        return new OrderedArraySet<>(Arrays.asList(elements));
    }

    static <T> OrderedSet<T> of(Collection<T> elements) {
        return new OrderedArraySet<>(elements);
    }

    static <T> OrderedSet<T> empty() {
        return (OrderedSet<T>) EMPTY_ORDERED_SET;
    }

    static <T> OrderedSet<T> create() {
        return new OrderedArraySet<>();
    }

    static <T> Collector<T, OrderedSet<T>, OrderedSet<T>> collector() {
        return (OrderedSetCollector<T>) COLLECTOR;
    }

    default T requireFirst() {
        return first().orElseThrow(() -> new IllegalStateException("This OrderedSet is empty"));
    }

    @NotNull Optional<T> first();

    @NotNull OrderedSet<T> copy();

    @NotNull List<T> list();

    @NotNull Set<T> set();

    default @NotNull OrderedSet<T> append(@NotNull Collection<T> elements) {
        addAll(elements);
        return this;
    }

    void set(@NotNull Collection<T> elements);

    void sinkInto(@NotNull Collection<T> other);

    @NotNull T get(int index);
}
