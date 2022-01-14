package com.github.thorbenkuck.ddt.api.registry;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.util.*;
import java.util.stream.Stream;

public class TestContentFactoryRegistry {

    private final Map<String, OrderedSet<TestCaseFactory<?, ?>>> mappedBySuite = new HashMap<>();
    private boolean setup = false;

    public void register(TestCaseFactory<?, ?> factory) {
        OrderedSet<TestCaseFactory<?, ?>> testContentFactories = mappedBySuite.computeIfAbsent(factory.suite(), suite -> OrderedSet.create());
        testContentFactories.add(factory);
    }

    public boolean isSetup() {
        return setup;
    }

    public void setupDone() {
        setup = true;
    }

    public OrderedSet<TestCaseFactory<?, ?>> findAllBy(TestScenario annotation) {
        if(noneRegistered(annotation)) {
            return OrderedSet.of();
        }
        return mappedBySuite.get(annotation.suite());
    }

    public Stream<TestCaseFactory<?, ?>> stream(TestScenario annotation) {
        if(noneRegistered(annotation)) {
            return Stream.empty();
        }

        return findAllBy(annotation)
                .stream();
    }

    public boolean anyRegistered(TestScenario annotation) {
        return mappedBySuite.containsKey(annotation.suite());
    }

    public boolean noneRegistered(TestScenario annotation) {
        return !anyRegistered(annotation);
    }
}
