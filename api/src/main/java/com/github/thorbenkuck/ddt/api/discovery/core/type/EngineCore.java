package com.github.thorbenkuck.ddt.api.discovery.core.type;

import com.github.thorbenkuck.ddt.api.discovery.core.DefaultEngineCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Properties;

public interface EngineCore extends EngineCoreInjector, EngineCoreLifecycle {

    default boolean singleton() {
        return false;
    }

    default void initialize(Class<?> clazz) {}

    default void tearDownTestInstance(@NotNull Object testInstance) {}

    Properties properties();

    EngineCore DEFAULT = new DefaultEngineCore();

    // Determination of engines

    EngineCoreDeterminationService determinationService = new EngineCoreDeterminationService();

    static EngineCore of(Class<?> testScenarioType) {
        return determinationService.determineFor(testScenarioType);
    }
}
