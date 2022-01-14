package com.github.thorbenkuck.ddt.junit.selectors;

import com.github.thorbenkuck.ddt.api.discovery.TestDiscoverer;
import org.junit.platform.engine.discovery.ClassSelector;

public class ClassSelectorProcessor implements SelectorProcessor<ClassSelector> {

    @Override
    public TestDiscoverer process(ClassSelector classSelector) {
        return TestDiscoverer.initialize()
                .appendForSearch(classSelector.getJavaClass());
    }
}
