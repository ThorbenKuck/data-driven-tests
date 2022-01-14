package com.github.thorbenkuck.ddt.api.domain.builder;

import com.github.thorbenkuck.ddt.api.domain.factory.FactoryTestCaseEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSuiteEntryBuilder<Input, Expected> {

    private final TestSuiteBuilder<Input, Expected> builder;
    private final List<Object> preconditions = new ArrayList<>();
    private final String name;

    public TestSuiteEntryBuilder(TestSuiteBuilder<Input, Expected> builder, String name) {
        this.builder = builder;
        this.name = name;
    }

    public TestSuiteEntryBuilder(TestSuiteBuilder<Input, Expected> builder) {
        this.builder = builder;
        this.name = null;
    }

    public TestSuiteEntryBuilder<Input, Expected> withPrecondition(Object precondition) {
        preconditions.add(precondition);

        return this;
    }

    public TestSuiteEntryBuilder<Input, Expected> withPreconditions(Object... precondition) {
        preconditions.addAll(Arrays.asList(precondition));

        return this;
    }

    public TestSuiteBuilder<Input, Expected> withInputOnly(Input input) {
        return builder.append(input);
    }

    public TestSuiteEntryBuilderStage forInput(Input input) {
        return new TestSuiteEntryBuilderStage(input);
    }

    public class TestSuiteEntryBuilderStage {
        private final Input input;

        public TestSuiteEntryBuilderStage(Input input) {
            this.input = input;
        }

        public TestSuiteBuilder<Input, Expected> expect(Expected expected) {
            return builder.append(
                    FactoryTestCaseEntry.<Input, Expected>build()
                            .withName(name)
                            .forInput(input)
                            .andAllPreconditions(preconditions)
                            .expects(expected)
                            .build()
            );
        }
    }
}
