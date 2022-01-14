package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.CollectedBy;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.TestCaseFactoryMetaData;
import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;
import com.github.thorbenkuck.ddt.api.domain.factory.FactoryTestCaseEntry;
import com.github.thorbenkuck.ddt.util.TestName;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@CollectedBy(TestCaseFactoryMetaData.class)
public interface TestCaseFactory<Input, Expected> {

    void setup(TestSuiteBuilder<Input, Expected> builder);

    default List<FactoryTestCaseEntry<? extends Input, ? extends Expected>> produce() {
        TestSuiteBuilder<Input, Expected> builder = newBuilder();
        setup(builder);
        return builder.build();
    }

    default String suite() {
        return "";
    }

    default Stream<FactoryTestCaseEntry<? extends Input, ? extends Expected>> produceStream() {
        return produce().stream();
    }

    default List<FactoryTestCaseEntry<Input, Expected>> list(FactoryTestCaseEntry<Input, Expected>... entries) {
        return Arrays.asList(entries);
    }

    default TestSuiteBuilder<Input, Expected> newBuilder() {
        return new TestSuiteBuilder<>();
    }

    default String name() {
        return TestName.produceOf(getClass().getSimpleName());
    }

    default FactoryTestCaseEntry.Builder<Input, Expected> newEntry() {
        return FactoryTestCaseEntry.build();
    }

    default FactoryTestCaseEntry.Builder<Input, Expected> newEntry(String name) {
        FactoryTestCaseEntry.Builder<Input, Expected> entry = newEntry();
        return entry.withName(name);
    }

    default FactoryTestCaseEntry<Input, Expected> newEntry(Consumer<FactoryTestCaseEntry.Builder<Input, Expected>> consumer) {
        FactoryTestCaseEntry.Builder<Input, Expected> builder = newEntry();
        consumer.accept(builder);
        return builder.build();
    }

    default FactoryTestCaseEntry<Input, Expected> newEntry(String name, Consumer<FactoryTestCaseEntry.Builder<Input, Expected>> consumer) {
        FactoryTestCaseEntry.Builder<Input, Expected> builder = newEntry();
        consumer.accept(builder.withName(name));
        return builder.build();
    }
}
