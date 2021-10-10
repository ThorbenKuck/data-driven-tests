package com.github.thorbenkuck.ddt.execution.context;

import com.github.thorbenkuck.ddt.Annotations;
import com.github.thorbenkuck.ddt.Reflections;
import com.github.thorbenkuck.ddt.api.annotations.AssertedBy;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.services.PluginDiscoverer;
import com.github.thorbenkuck.ddt.DdtProperties;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ExecutionContextAsserters {

    private final List<Asserter> asserters;

    public ExecutionContextAsserters(TestScenario annotation, Class<?> testScenario, Method method) {
        List<Class<? extends Asserter>> classes = extractAsserters(annotation, method);
        if (DdtProperties.skipFailedAsserterInstantiation()) {
            asserters = createLenient(classes, testScenario, method);
        } else {
            asserters = createStrict(classes, testScenario, method);
        }
        if (asserters.isEmpty()) {
            asserters.addAll(PluginDiscoverer.loadAllAsserters().access());
        }
    }

    public List<Asserter> getAsserters() {
        return Collections.unmodifiableList(asserters);
    }

    private List<Asserter> createStrict(List<Class<? extends Asserter>> classes, Class<?> testScenario, Method method) {
        return classes.stream()
                .map(it -> Reflections.newInstance(it, testScenario, method))
                .collect(Collectors.toList());
    }

    private List<Asserter> createLenient(List<Class<? extends Asserter>> classes, Class<?> testScenario, Method method) {
        return classes.stream()
                .map(it -> {
                    try {
                        return Reflections.newInstance(it, testScenario, method);
                    } catch (Throwable e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Class<? extends Asserter>> extractAsserters(TestScenario testScenario, Method method) {
        Set<Class<? extends Asserter>> asserters = new HashSet<>(Arrays.asList(testScenario.asserters()));

        Annotations.deepFindIn(method, AssertedBy.class)
                .forEach(annotation -> asserters.addAll(Arrays.asList(annotation.value())));

        if (asserters.isEmpty()) {
            DdtProperties.fallbackAsserter()
                    .ifPresent(property -> {
                        Arrays.stream(property.split(","))
                                .map(current -> {
                                    try {
                                        Class<?> declaredDefault = Class.forName(current);
                                        if (!Asserter.class.isAssignableFrom(declaredDefault)) {
                                            throw new IllegalArgumentException("The class " + declaredDefault + " is not implementing the Asserter interface");
                                        }
                                        return (Class<? extends Asserter>) declaredDefault;
                                    } catch (ClassNotFoundException e) {
                                        throw new IllegalStateException("Could not find the default asserter class " + current, e);
                                    }
                                }).forEach(asserters::add);
                    });
        }

        return new ArrayList<>(asserters);
    }

}
