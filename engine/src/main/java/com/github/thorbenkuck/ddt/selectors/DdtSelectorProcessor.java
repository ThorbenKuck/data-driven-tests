package com.github.thorbenkuck.ddt.selectors;

import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;

import java.util.List;

public interface DdtSelectorProcessor<T extends DiscoverySelector> {

    List<TestDescriptor> process(UniqueId uniqueId, T selector);

}
