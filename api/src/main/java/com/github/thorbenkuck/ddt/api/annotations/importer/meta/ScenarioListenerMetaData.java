package com.github.thorbenkuck.ddt.api.annotations.importer.meta;

import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioListener;

import java.lang.reflect.Method;

public class ScenarioListenerMetaData {

    private final OrderedSet<ScenarioListener> scenarioListenerList;

    public ScenarioListenerMetaData(OrderedSet<ScenarioListener> scenarioListenerList) {
        this.scenarioListenerList = scenarioListenerList;
    }

    public static ScenarioListenerMetaData of(Method method) {
        return AnnotationCollector.of(ScenarioListener.class)
                .searchIn(method)
                .searchIn(method.getDeclaringClass())
                .analyze()
                .sinkInto(ScenarioListenerMetaData::new);
    }

    public OrderedSet<ScenarioListener> getScenarioListenerList() {
        return scenarioListenerList;
    }
}
