package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.services.adapter.TypeConverterAdapterRegistry;

import java.util.Optional;

public abstract class AbstractTestCaseContent<I extends TestCaseInput, O extends TestCaseOutput> implements TestCaseContent {

    private final I testCaseInput;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<O> testCaseOutput;

    public AbstractTestCaseContent(I testCaseInput, TestScenario annotation) {
        this.testCaseInput = testCaseInput;
        this.testCaseOutput = resolveExpectedOutput(testCaseInput, annotation);
    }

    protected abstract Optional<O> resolveExpectedOutput(I testCaseInput, TestScenario annotation);

    @Override
    public String rawName() {
        return testCaseInput.rawName();
    }

    @Override
    public I input() {
        return testCaseInput;
    }

    @Override
    public <T> T convertInputTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry) {
        return input().convertTo(type, adapterRegistry);
    }

    @Override
    public <T> T convertOutputTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry) {
        return output().convertTo(type, adapterRegistry);
    }

    @Override
    public O output() {
        return testCaseOutput.orElseThrow(() -> new IllegalStateException("No expected output present."));
    }

    public boolean outputFileExists() {
        return testCaseOutput.isPresent();
    }
}
