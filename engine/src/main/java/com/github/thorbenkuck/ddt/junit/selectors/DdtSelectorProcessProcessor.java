package com.github.thorbenkuck.ddt.junit.selectors;

import com.github.thorbenkuck.ddt.api.discovery.TestDiscoverer;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestClassDescriptor;
import com.github.thorbenkuck.ddt.junit.descriptors.JunitTestClassDdtDescriptor;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;
import org.junit.platform.engine.discovery.FileSelector;
import org.junit.platform.engine.discovery.MethodSelector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DdtSelectorProcessProcessor {

    private final Map<Class<? extends DiscoverySelector>, SelectorProcessor<? extends DiscoverySelector>> selectorMap = new HashMap<>();

    public List<TestDescriptor> processAll(EngineDiscoveryRequest discoveryRequest, UniqueId engineId) {
        TestDiscoverer discoverer = TestDiscoverer.initialize();

        selectorMap.forEach((key, value) -> {
            List<? extends DiscoverySelector> selector = discoveryRequest.getSelectorsByType(key);
            selector.forEach(it -> discoverer.expandWith(doSingleProcess(it, value)));
        });

        return map(discoverer, engineId);
    }

    public <T extends DiscoverySelector> void register(Class<T> type, SelectorProcessor<T> processor) {
        selectorMap.put(type, processor);
    }

    private List<TestDescriptor> map(TestDiscoverer discoverer, UniqueId engineId) {
        final List<TestDescriptor> result = new ArrayList<>();

        discoverer.findAll()
                .stream()
                .map(it -> map(it, engineId))
                .forEach(result::add);

        discoverer.clear();
        return result;
    }

    private TestDescriptor map(TestClassDescriptor testClassDescriptor, UniqueId engineId) {
        return new JunitTestClassDdtDescriptor(engineId, testClassDescriptor);
    }

    private <T extends DiscoverySelector> TestDiscoverer doSingleProcess(DiscoverySelector t, SelectorProcessor<T> selector) {
        return selector.process((T) t);
    }
}
