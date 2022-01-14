package com.github.thorbenkuck.ddt.api.annotations.importer.meta;

import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioContext;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;

import java.util.Optional;
import java.util.function.Function;

public class ScenarioContextMetaData {

    private final Class<? extends EngineCore> testScenarioRootContext;

    public ScenarioContextMetaData(Class<? extends EngineCore> testScenarioRootContext) {
        this.testScenarioRootContext = testScenarioRootContext;
    }

    public static ScenarioContextMetaData of(Class<?> type) {
        return AnnotationCollector.of(ScenarioContext.class)
                .findFirst(type)
                .map(ScenarioContext::value)
                .map(ScenarioContextMetaData::new)
                .orElse(new ScenarioContextMetaData(null));
    }

    public <T> Optional<T> map(Function<Class<? extends EngineCore>, T> function) {
        return Optional.ofNullable(testScenarioRootContext)
                .map(function);
    }

    public Optional<Class<? extends EngineCore>> value() {
        return Optional.ofNullable(testScenarioRootContext);
    }
}
