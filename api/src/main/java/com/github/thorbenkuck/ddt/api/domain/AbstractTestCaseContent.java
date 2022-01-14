package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.registry.TypeConverterAdapterRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractTestCaseContent<I extends TestCaseInput, O extends TestCaseOutput> implements TestCaseContent {

    private final I testCaseInput;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<O> testCaseOutput;
    private final List<Object> preconditions;
    protected final TestScenario annotation;

    public AbstractTestCaseContent(I testCaseInput, TestScenario annotation) {
        this(testCaseInput, annotation, new ArrayList<>());
    }

    public AbstractTestCaseContent(I testCaseInput, TestScenario annotation, List<Object> preconditions) {
        this.testCaseInput = testCaseInput;
        this.testCaseOutput = resolveExpectedOutput(testCaseInput, annotation);
        this.preconditions = preconditions;
        this.annotation = annotation;
    }

    protected abstract Optional<O> resolveExpectedOutput(I testCaseInput, TestScenario annotation);

    @Override
    public List<Object> preconditions() {
        return preconditions;
    }

    @Override
    public void addPreconditions(List<Object> preconditions) {
        this.preconditions.addAll(preconditions);
    }

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
