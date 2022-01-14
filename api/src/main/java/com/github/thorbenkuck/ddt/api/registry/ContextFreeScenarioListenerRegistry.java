package com.github.thorbenkuck.ddt.api.registry;

import com.github.thorbenkuck.ddt.api.domain.listener.ContextFreeScenarioListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContextFreeScenarioListenerRegistry {

    private final List<ContextFreeScenarioListener> listeners = new ArrayList<>();
    private boolean setup = false;

    public void register(ContextFreeScenarioListener listener) {
        listeners.add(listener);
    }

    public List<ContextFreeScenarioListener> access() {
        return Collections.unmodifiableList(listeners);
    }

    public boolean isSetup() {
        return setup;
    }

    public void setupDone() {
        setup = true;
    }
}
