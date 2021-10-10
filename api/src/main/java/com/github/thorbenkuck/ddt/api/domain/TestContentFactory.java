package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;
import com.github.thorbenkuck.ddt.api.domain.factory.TestCaseEntry;
import com.github.thorbenkuck.ddt.TestName;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface TestContentFactory<Input, Expected> {

    List<TestCaseEntry<Input, Expected>> produce();

    String suite();

    default Stream<TestCaseEntry<Input, Expected>> stream() {
        return produce().stream();
    }

    default List<TestCaseEntry<Input, Expected>> list(TestCaseEntry<Input, Expected>... entries) {
        return Arrays.asList(entries);
    }

    default TestSuiteBuilder<Input, Expected> builder() {
        return new TestSuiteBuilder<>();
    }

    default String name() {
        return TestName.produceOf(getClass().getSimpleName());
    }

    default TestCaseEntry.Builder<Input, Expected> entry() {
        return TestCaseEntry.build();
    }

    default TestCaseEntry.Builder<Input, Expected> entry(String name) {
        TestCaseEntry.Builder<Input, Expected> entry = entry();
        return entry.withName(name);
    }

    default TestCaseEntry<Input, Expected> entry(Consumer<TestCaseEntry.Builder<Input, Expected>> consumer) {
        TestCaseEntry.Builder<Input, Expected> builder = entry();
        consumer.accept(builder);
        return builder.build();
    }

    default TestCaseEntry<Input, Expected> entry(String name, Consumer<TestCaseEntry.Builder<Input, Expected>> consumer) {
        TestCaseEntry.Builder<Input, Expected> builder = entry();
        consumer.accept(builder.withName(name));
        return builder.build();
    }
}
