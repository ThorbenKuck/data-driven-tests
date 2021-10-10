package com.github.thorbenkuck.ddt.api.domain.factory;

import java.util.Objects;
import java.util.Optional;

public class TestCaseEntry<Input, Expected> {
    private final Input input;
    private final Expected expected;
    private final String name;

    private TestCaseEntry(Input input, Expected expected, String name) {
        this.input = input;
        this.expected = expected;
        this.name = name;
    }

    public static <Input, Expected> Builder<Input, Expected> build() {
        return new Builder<>();
    }

    public Input getInput() {
        return input;
    }

    public Optional<Expected> getExpected() {
        return Optional.ofNullable(expected);
    }

    public boolean hasOutput() {
        return getExpected().isPresent();
    }

    public String getName() {
        return name;
    }

    public static class Builder<Input, Expected> {

        private Input input;
        private Expected expected;
        private String name = "";

        public <T extends Input> Builder<Input, Expected> forInput(T input) {
            Objects.requireNonNull(input);
            this.input = input;
            return this;
        }

        public <T extends Expected> Builder<Input, Expected> expects(T expected) {
            Objects.requireNonNull(expected);
            this.expected = expected;
            return this;
        }

        public Builder<Input, Expected> withName(String name) {
            Objects.requireNonNull(name);
            this.name = name;
            return this;
        }

        public TestCaseEntry<Input, Expected> build() {
            if(input == null) {
                throw new IllegalStateException("An input instance has to be provided");
            }
            return new TestCaseEntry<>(input, expected, name);
        }
    }
}