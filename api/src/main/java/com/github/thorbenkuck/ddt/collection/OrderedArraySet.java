package com.github.thorbenkuck.ddt.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrderedArraySet<T> implements OrderedSet<T> {

    private final List<T> content;

    public OrderedArraySet(Collection<T> content) {
        this.content = new ArrayList<>(content);
    }

    public OrderedArraySet() {
        this.content = new ArrayList<>();
    }

    @Override
    public int size() {
        return content.size();
    }

    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return content.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return content.toArray();
    }

    @NotNull
    @Override
    public <S> S[] toArray(@NotNull S[] typeArray) {
        return content.toArray(typeArray);
    }

    public @NotNull T get(int index) {
        return content.get(index);
    }

    @Override
    public boolean add(T e) {
        if (content.contains(e)) {
            return false;
        }
        return content.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return content.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        return content.contains(collection);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        AtomicBoolean allAdded = new AtomicBoolean(true);

        c.forEach(entry -> {
            if (!add(entry)) {
                allAdded.set(false);
            }
        });

        return allAdded.get();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        return content.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        return content.retainAll(collection);
    }

    @Override
    public void clear() {
        content.clear();
    }

    @Override
    public @NotNull Optional<T> first() {
        if (content.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(content.get(0));
        }
    }

    @Override
    public @NotNull OrderedSet<T> copy() {
        return new OrderedArraySet<>(content);
    }

    @Override
    public @NotNull List<T> list() {
        return new ArrayList<>(content);
    }

    @Override
    public @NotNull Set<T> set() {
        return new HashSet<>(content);
    }

    @Override
    public void set(@NotNull Collection<T> elements) {
        clear();
        addAll(elements);
    }

    @Override
    public void sinkInto(@NotNull Collection<T> other) {
        other.addAll(this);
        clear();
    }
}
