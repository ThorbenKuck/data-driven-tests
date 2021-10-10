package com.github.thorbenkuck.ddt.api.domain.builder;

import com.github.thorbenkuck.ddt.api.domain.factory.TestCaseEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestSuiteBuilder<Input, Expected> {
        private final List<TestCaseEntry<Input, Expected>> content = new ArrayList<>();

        public TestSuiteBuilder<Input, Expected> append(Input input) {
            return append(TestCaseEntry.<Input, Expected>build().forInput(input));
        }

        public TestSuiteBuilder<Input, Expected> addEntry(String name, Consumer<TestSuiteEntryBuilder<Input, Expected>> consumer) {
            consumer.accept(addEntry(name));
            return this;
        }

        public TestSuiteBuilder<Input, Expected> addEntry(Consumer<TestSuiteEntryBuilder<Input, Expected>> consumer) {
            consumer.accept(addEntry());
            return this;
        }

        public TestSuiteEntryBuilder<Input, Expected> addEntry(String name) {
            return new TestSuiteEntryBuilder<>(this, name);
        }

        public TestSuiteEntryBuilder<Input, Expected> addEntry() {
            return new TestSuiteEntryBuilder<>(this);
        }

        public TestSuiteBuilder<Input, Expected> append(Input input, Expected expected) {
            TestCaseEntry.<Input, Expected>build().forInput(input).expects(expected);
            return append(TestCaseEntry.<Input, Expected>build().forInput(input).expects(expected));
        }

        public TestSuiteBuilder<Input, Expected> append(TestCaseEntry.Builder<Input, Expected> factoryResultBuilder) {
            content.add(factoryResultBuilder.build());
            return this;
        }

        public TestSuiteBuilder<Input, Expected> append(TestCaseEntry<Input, Expected> factoryResult) {
            content.add(factoryResult);
            return this;
        }

        public List<TestCaseEntry<Input, Expected>> build() {
            List<TestCaseEntry<Input, Expected>> result = new ArrayList<>(content);
            content.clear();

            return result;
        }
    }