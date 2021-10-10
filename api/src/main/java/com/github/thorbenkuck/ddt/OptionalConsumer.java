package com.github.thorbenkuck.ddt;

import java.util.function.Consumer;

public class OptionalConsumer<T> {

    private final T t;

    private OptionalConsumer(T t) {
        this.t = t;
    }

    public static <T> OptionalConsumer<T> of(T t) {
        return new OptionalConsumer<>(t);
    }

    public OrStage ifPresent(Consumer<T> consumer) {
        return new OrStage(consumer);
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
