package com.github.thorbenkuck.ddt.util;

import java.util.function.Consumer;
import java.util.function.Function;

public class OptionalConsumer<T> {

    private final T t;

    private OptionalConsumer(T t) {
        this.t = t;
    }

    public static <T> OptionalConsumer<T> of(T t) {
        return new OptionalConsumer<>(t);
    }

    public static <T> OptionalConsumer<T> empty() {
        return new OptionalConsumer<>(null);
    }

    public OrStage ifPresent(Consumer<T> consumer) {
        return new OrStage(consumer);
    }

    public <S> OptionalConsumer<S> map(Function<T, S> function) {
        if(t != null) {
            S mappedValue = function.apply(t);
            return OptionalConsumer.of(mappedValue);
        } else {
            return OptionalConsumer.empty();
        }
    }

    public class OrStage {
        private final Consumer<T> consumer;

        OrStage(Consumer<T> consumer) {
            this.consumer = consumer;
        }

        public void orElse(Runnable runnable) {
            if(t != null) {
                consumer.accept(t);
            } else {
                runnable.run();
            }
        }
    }
}
