package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import com.github.thorbenkuck.ddt.api.annotations.TestScenarioValues;
import com.github.thorbenkuck.ddt.properties.DdtProperties;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.AsserterMetaData;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.registry.PluginDiscoverer;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AsserterDescriptor {

    public static OrderedSet<Asserter> findAll(TestScenarioValues annotation, Class<?> testScenario, Method method, EngineCore engineCore) {
        final OrderedSet<Class<? extends Asserter>> classes = AsserterMetaData.of(method, annotation).getAsserters();
        final OrderedSet<Asserter> asserters;

        if (DdtProperties.skipFailedAsserterInstantiation()) {
            asserters = createLenient(classes, engineCore);
        } else {
            asserters = createStrict(classes, engineCore);
        }

        if (asserters.isEmpty()) {
            asserters.addAll(PluginDiscoverer.loadAllAsserters(engineCore).access());
        }

        return asserters;
    }

    private static OrderedSet<Asserter> createStrict(OrderedSet<Class<? extends Asserter>> classes, EngineCore engineCore) {
        return classes.stream()
                .map(engineCore::createInstanceOf)
                .collect(OrderedSet.collector());
    }

    private static OrderedSet<Asserter> createLenient(OrderedSet<Class<? extends Asserter>> classes, EngineCore engineCore) {
        return classes.stream()
                .map(it -> {
                    try {
                        return engineCore.createInstanceOf(it);
                    } catch (Throwable e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(OrderedSet.collector());
    }
}
