package com.github.thorbenkuck.ddt.api.services.factory;

import com.github.thorbenkuck.ddt.api.domain.TestContentFactory;

import java.util.*;

public class TestContentFactoryRegistry {

    private final Map<String, List<TestContentFactory<?, ?>>> content = new HashMap<>();
    private boolean setup = false;

    public void register(TestContentFactory<?, ?> factory) {
        List<TestContentFactory<?, ?>> testContentFactories = content.computeIfAbsent(factory.suite(), suite -> new ArrayList<>());
        testContentFactories.add(factory);
    }

    public List<TestContentFactory<?, ?>> findAllForSuite(String suite) {
        return Optional.ofNullable(content.get(suite))
                .orElse(Collections.emptyList());
    }

    public boolean isSetup() {
        return setup;
    }

    public void setupDone() {
        setup = true;
    }
}
