package com.github.thorbenkuck.ddt.execution.context;

import com.github.thorbenkuck.ddt.Annotations;
import com.github.thorbenkuck.ddt.Reflections;
import com.github.thorbenkuck.ddt.api.annotations.ScenarioListener;
import com.github.thorbenkuck.ddt.api.domain.TestScenarioListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class ExecutionContextScenarioListeners {

    private final List<TestScenarioListener> scenarioListeners;

    public ExecutionContextScenarioListeners(Class<?> type, Method method) {
        scenarioListeners = determineAllTestScenarioListeners(type, method);
    }

    public List<TestScenarioListener> getScenarioListeners() {
        return Collections.unmodifiableList(scenarioListeners);
    }

    private List<TestScenarioListener> determineAllTestScenarioListeners(Class<?> type, Method method) {
        return findInParentAndMethod(type, method, ScenarioListener.class, annotation -> Arrays.stream(annotation.value()));
    }

    private <A extends Annotation, T> List<T> findInParentAndMethod(Class<?> scenarioType, Method method, Class<A> type, Function<A, Stream<Class<? extends T>>> translation) {
        final Set<T> result = new HashSet<>();

        Annotations.deepFindIn(method, type)
                .stream()
                .flatMap(translation)
                .map(it -> Reflections.newInstance(it, scenarioType, method))
                .forEach(result::add);

        Annotations.deepFindIn(scenarioType, type)
                .stream()
                .flatMap(translation)
                .map(it -> Reflections.newInstance(it, scenarioType, method))
                .forEach(result::add);

        return new ArrayList<>(result);
    }

}
