package com.github.thorbenkuck.ddt.api.discovery.core.type;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.ScenarioContextMetaData;
import com.github.thorbenkuck.ddt.api.registry.PluginDiscoverer;
import com.github.thorbenkuck.ddt.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class EngineCoreDeterminationService {

    private static final Map<Class<?>, Supplier<EngineCore>> instanceSupplier = new HashMap<>();

    public EngineCore determineFor(Class<?> testScenarioType) {
        Optional<EngineCore> rootContextOnClass = ScenarioContextMetaData.of(testScenarioType)
                .map(it -> createInstance(it, testScenarioType));

        if(rootContextOnClass.isPresent()) {
            return rootContextOnClass.get();
        }
        Optional<EngineCore> rootContextFromPlugins = PluginDiscoverer.loadDefaultTestScenarioContext()
                .map(it -> {
                    Reflections.invokeInheritedMethods(it, testScenarioType);
                    return it;
                });

        return rootContextFromPlugins.orElse(EngineCore.DEFAULT);

    }

    private static EngineCore createInstance(Class<? extends EngineCore> type, Class<?> testScenarioType) {
        EngineCore engineCore;

        if (instanceSupplier.containsKey(type)) {
            engineCore = instanceSupplier.get(type).get();
        } else {
            engineCore = Reflections.newTypedInstance(EngineCore.class, type, testScenarioType);
            if (engineCore.singleton()) {
                instanceSupplier.put(type, () -> engineCore);
            }

            engineCore.postConstruct();
            engineCore.initialize(testScenarioType);
        }

        Reflections.invokeInheritedMethods(engineCore);
        return engineCore;
    }
}
