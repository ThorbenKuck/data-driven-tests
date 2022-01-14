package com.github.thorbenkuck.ddt.api.domain.listener;

import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioListener;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestClassDescriptor;
import com.github.thorbenkuck.ddt.api.registry.ContextFreeScenarioListenerRegistry;
import com.github.thorbenkuck.ddt.api.registry.PluginDiscoverer;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

public interface ContextFreeScenarioListener {

    default void initialization() {
    }

    default void engineCoreInitialized(EngineCore engineCore) {
    }

    default void discoveryStarted() {
    }

    default void discoveryFinished(OrderedSet<TestClassDescriptor> testClassDescriptor) {
    }


    default void beforeClass() {
    }

    default void beforeMethod() {
    }

    default void beforeCase() {
    }

    default void afterCase() {
    }

    default void afterMethod() {
    }

    default void afterClass() {
    }

    static OrderedSet<ContextFreeScenarioListener> findAll(Class<?> type) {
        return AnnotationCollector.of(ScenarioListener.class)
                .searchIn(type)
                .analyze()
                .flatMap(ScenarioListener::contextFree)
                .mapNotNull(it -> {
                    try {
                        return it.getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        return null;
                    }
                })
                .map(it -> (ContextFreeScenarioListener) it)
                .asOrderedSet()
                .append(PluginDiscoverer.loadContextFreeScenarioListeners());
    }
}
