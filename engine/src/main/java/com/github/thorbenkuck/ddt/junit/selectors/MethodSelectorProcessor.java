package com.github.thorbenkuck.ddt.junit.selectors;

import com.github.thorbenkuck.ddt.api.discovery.TestDiscoverer;
import org.junit.platform.engine.discovery.MethodSelector;

public class MethodSelectorProcessor implements SelectorProcessor<MethodSelector> {

    @Override
    public TestDiscoverer process(MethodSelector methodSelector) {
        return TestDiscoverer.initialize()
                .appendForSearch(methodSelector.getJavaMethod());
    }
}
