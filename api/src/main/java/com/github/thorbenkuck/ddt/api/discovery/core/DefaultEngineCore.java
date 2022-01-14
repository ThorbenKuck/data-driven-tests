package com.github.thorbenkuck.ddt.api.discovery.core;

import com.github.thorbenkuck.ddt.reflections.Reflections;

public class DefaultEngineCore extends AbstractEngineCore {

    @Override
    public <T> T createInstanceOf(Class<T> scenarioType) {
        return Reflections.newInstance(scenarioType);
    }

}
