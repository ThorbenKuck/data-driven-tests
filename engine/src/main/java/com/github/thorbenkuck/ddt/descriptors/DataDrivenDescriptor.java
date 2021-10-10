package com.github.thorbenkuck.ddt.descriptors;

import org.junit.platform.engine.TestDescriptor;

public interface DataDrivenDescriptor extends TestDescriptor {

    void started();

    void ended();

    default TestDescriptor resolve() {
        return this;
    }

}
