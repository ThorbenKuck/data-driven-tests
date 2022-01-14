package com.github.thorbenkuck.ddt.api.domain.builder;

import com.github.thorbenkuck.ddt.api.domain.factory.FactoryTestCaseEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestSuiteBuilder<Input, Expected> {
    private final List<FactoryTestCaseEntry<? extends Input, ? extends Expected>> content = new ArrayList<>();

    public<T extends Input,S extends Expected> TestSuiteBuilder<Input, Expected> mergeWith(TestSuiteBuilder<T, S> other) {
        TestSuiteBuilder<Input, Expected> combinedBuilder = new TestSuiteBuilder<>();
        combinedBuilder.content.addAll(this.content);
        combinedBuilder.content.addAll(other.content);
        return combinedBuilder;
    }

    public <T extends Input> TestSuiteBuilder<Input, Expected> append(T input) {
        return append(FactoryTestCaseEntry.<Input, Expected>build().forInput(input));
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

    public <T extends Input, S extends Expected> TestSuiteBuilder<Input, Expected> append(T input, S expected) {
        FactoryTestCaseEntry.Builder<Input, Expected> expectedResult = FactoryTestCaseEntry.<Input, Expected>build().forInput(input).expects(expected);
        return append(expectedResult);
    }

    public TestSuiteBuilder<Input, Expected> append(FactoryTestCaseEntry.Builder<Input, Expected> factoryResultBuilder) {
        content.add(factoryResultBuilder.build());
        return this;
    }

    public TestSuiteBuilder<Input, Expected> append(FactoryTestCaseEntry<Input, Expected> factoryResult) {
        content.add(factoryResult);
        return this;
    }

    public List<FactoryTestCaseEntry<? extends Input, ? extends Expected>> build() {
        List<FactoryTestCaseEntry<? extends Input, ? extends Expected>> result = new ArrayList<>(content);
        content.clear();
        return result;
    }
}