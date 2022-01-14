package com.github.thorbenkuck.ddt.junit.selectors;

import com.github.thorbenkuck.ddt.api.discovery.TestDiscoverer;
import org.junit.platform.engine.DiscoverySelector;

public interface SelectorProcessor<T extends DiscoverySelector> {

    TestDiscoverer process(T selector);

}
