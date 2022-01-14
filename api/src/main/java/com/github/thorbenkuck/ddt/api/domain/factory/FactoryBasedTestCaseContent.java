package com.github.thorbenkuck.ddt.api.domain.factory;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.AbstractTestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class FactoryBasedTestCaseContent<T, S> extends AbstractTestCaseContent<FactoryBasedTestCaseInput<T, S>, FactoryBasedTestCaseOutput<S>> {

    private final Class<?> factoryType;
    private final FactoryTestCaseEntry<T, S> factoryResult;

    public FactoryBasedTestCaseContent(
            Class<?> factoryType,
            FactoryTestCaseEntry<T, S> factoryResult,
            TestScenario annotation
    ) {
        super(new FactoryBasedTestCaseInput<>(factoryResult), annotation, factoryResult.getPreconditions());
        this.factoryType = factoryType;
        this.factoryResult = factoryResult;
    }

    @Override
    protected Optional<FactoryBasedTestCaseOutput<S>> resolveExpectedOutput(FactoryBasedTestCaseInput<T, S> testCaseInput, TestScenario annotation) {
        return testCaseInput.getFactoryResult().getExpected()
                .map(FactoryBasedTestCaseOutput::new);
    }

    @Override
    public TestSource testSource() {
        return ClassSource.from(factoryType);
    }

    @Override
    public void validateMatchesMethod(Method method) {
        Class<?> inputType = input().classType();
        Class<?> outputType = output().classType();
        if(method.getParameterCount() == 1) {
            if(!method.getParameterTypes()[0].isAssignableFrom(inputType)) {
                throw new IllegalStateException("Cannot assign the type " + method.getParameterTypes()[0] + " to " + inputType);
            }
            if(!method.getReturnType().equals(Void.TYPE)) {
                if(!factoryResult.hasOutput()) {
                    throw new IllegalStateException("Factory does not provide the required return value!");
                }
                if(!method.getReturnType().isAssignableFrom(outputType)) {
                    throw new IllegalStateException("Factory provides a return value of type " + outputType+ ", but " + method.getReturnType() + " was expected by the test");
                }
            }
        }

        if(method.getParameterCount() == 2) {
            if (!method.getParameterTypes()[0].isAssignableFrom(inputType)) {
                throw new IllegalStateException("Cannot assign the first parameter of type " + method.getParameterTypes()[0] + " to " + inputType + ", which was provided by the factory");
            }
            if (!method.getParameterTypes()[1].isAssignableFrom(outputType)) {
                throw new IllegalStateException("Cannot assign the second parameter of type " + method.getParameterTypes()[1] + " to " + outputType + ", which was provided by the factory");
            }
        }
        // TODO
    }
}
