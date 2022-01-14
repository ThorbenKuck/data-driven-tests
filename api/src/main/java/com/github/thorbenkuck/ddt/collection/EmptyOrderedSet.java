package com.github.thorbenkuck.ddt.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmptyOrderedSet<T> implements OrderedSet<T> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new EmptyIterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NotNull
    @Override
    public <S> S[] toArray(@NotNull S[] typeArray) {
        return typeArray;
    }

    @NotNull
    public T get(int index) {
        throw new IllegalArgumentException("Index " + index + " does not exist in " + this);
    }

    @Override
    public boolean add(T e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public @NotNull Optional<T> first() {
        return Optional.empty();
    }

    @Override
    public @NotNull OrderedSet<T> copy() {
        return new EmptyOrderedSet<>();
    }

    @Override
    public @NotNull List<T> list() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull Set<T> set() {
        return new HashSet<>();
    }

    @Override
    public void set(@NotNull Collection<T> elements) {
    }

    @Override
    public void sinkInto(@NotNull Collection<T> other) {
    }

    private class EmptyIterator implements Iterator<T> {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            throw new UnsupportedOperationException();
        }
    }
}
