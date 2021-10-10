package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.domain.factory.TestCaseEntry;
import com.github.thorbenkuck.ddt.TestName;

import java.util.ArrayList;
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

    default TestContentFactoryBuilder<Input, Expected> builder() {
        return new TestContentFactoryBuilder<>();
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

    class TestContentFactoryBuilder<Input, Expected> {
        private final List<TestCaseEntry<Input, Expected>> content = new ArrayList<>();

        public TestContentFactoryBuilder<Input, Expected> append(Input input) {
            return append(TestCaseEntry.<Input, Expected>build().forInput(input));
        }

        public TestContentFactoryBuilder<Input, Expected> append(Input input, Expected expected) {
            TestCaseEntry.<Input, Expected>build().forInput(input).expects(expected);
            return append(TestCaseEntry.<Input, Expected>build().forInput(input).expects(expected));
        }

        public TestContentFactoryBuilder<Input, Expected> append(TestCaseEntry.Builder<Input, Expected> factoryResultBuilder) {
            content.add(factoryResultBuilder.build());
            return this;
        }

        public TestContentFactoryBuilder<Input, Expected> append(TestCaseEntry<Input, Expected> factoryResult) {
            content.add(factoryResult);
            return this;
        }

        public List<TestCaseEntry<Input, Expected>> build() {
            List<TestCaseEntry<Input, Expected>> result = new ArrayList<>(content);
            content.clear();

            return result;
        }
    }

}
