package com.github.thorbenkuck.ddt.api.domain.builder;

import com.github.thorbenkuck.ddt.api.domain.factory.TestCaseEntry;

public class TestSuiteEntryBuilder<Input, Expected> {

    private final TestSuiteBuilder<Input, Expected> builder;
    private final String name;

    public TestSuiteEntryBuilder(TestSuiteBuilder<Input, Expected> builder, String name) {
        this.builder = builder;
        this.name = name;
    }

    public TestSuiteEntryBuilder(TestSuiteBuilder<Input, Expected> builder) {
        this.builder = builder;
        this.name = null;
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

        public TestSuiteBuilder<Input, Expected> expects(Expected expected) {
            return builder.append(
                    TestCaseEntry.<Input, Expected>build()
                            .forInput(input)
                            .expects(expected)
                            .build()
            );
        }
    }
}
