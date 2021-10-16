package com.github.thorbenkuck.ddt.selectors;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DdtSelectorProcessProcessor {

    private final Map<Class<? extends DiscoverySelector>, SelectorProcessor<? extends DiscoverySelector>> selectorMap = new HashMap<>();

    public List<TestDescriptor> processAll(EngineDiscoveryRequest discoveryRequest, UniqueId engineId) {
        List<TestDescriptor> descriptors = new ArrayList<>();
        selectorMap.forEach((key, value) -> {
            List<? extends DiscoverySelector> selector = discoveryRequest.getSelectorsByType(key);
            selector.forEach(it -> descriptors.addAll(doSingleProcess(it, value, engineId)));
        });

        return descriptors;
    }

    public <T extends DiscoverySelector> void register(Class<T> type, SelectorProcessor<T> processor) {
        selectorMap.put(type, processor);
    }

    private <T extends DiscoverySelector> List<TestDescriptor> doSingleProcess(DiscoverySelector t, SelectorProcessor<T> selector, UniqueId uniqueId) {
        return selector.process(uniqueId, (T) t);
    }

}
