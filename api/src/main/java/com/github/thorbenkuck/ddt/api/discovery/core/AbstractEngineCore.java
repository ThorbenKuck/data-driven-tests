package com.github.thorbenkuck.ddt.api.discovery.core;

import com.github.thorbenkuck.ddt.api.annotations.marker.Singleton;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.properties.DdtProperties;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;

import java.util.Properties;

public abstract class AbstractEngineCore implements EngineCore {

    private final Properties properties = new Properties();

    @Override
    public boolean singleton() {
        return AnnotationCollector.findFirst(Singleton.class, getClass())
                .map(Singleton::value)
                .orElseGet(DdtProperties::isEngineCoreSingleton);
    }

    @Override
    public Properties properties() {
        return properties;
    }
}
