package com.github.thorbenkuck.ddt.api.domain.factory;

import java.util.*;

public class FactoryTestCaseEntry<Input, Expected> {
    private final Input input;
    private final Expected expected;
    private final String name;
    private final List<Object> preconditions;

    private FactoryTestCaseEntry(Input input, Expected expected, String name, List<Object> preconditions) {
        this.input = input;
        this.expected = expected;
        this.name = name;
        this.preconditions = preconditions;
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

    public List<Object> getPreconditions() {
        return preconditions;
    }

    public static class Builder<Input, Expected> {

        private Input input;
        private Expected expected;
        private final List<Object> preconditions = new ArrayList<>();
        private String name = "";

        public <T extends Input> Builder<Input, Expected> forInput(T input) {
            Objects.requireNonNull(input);
            this.input = input;
            return this;
        }

        public Builder<Input, Expected> andPrecondition(Object o) {
            Objects.requireNonNull(o);
            preconditions.add(o);
            return this;
        }

        public Builder<Input, Expected> andPreconditions(Object first, Object... o) {
            preconditions.add(first);
            preconditions.addAll(Arrays.asList(o));
            return this;
        }

        public Builder<Input, Expected> andAllPreconditions(List<Object> o) {
            preconditions.addAll(o);
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

        public FactoryTestCaseEntry<Input, Expected> build() {
            if(input == null) {
                throw new IllegalStateException("An input instance has to be provided");
            }
            return new FactoryTestCaseEntry<>(input, expected, name, preconditions);
        }
    }
}