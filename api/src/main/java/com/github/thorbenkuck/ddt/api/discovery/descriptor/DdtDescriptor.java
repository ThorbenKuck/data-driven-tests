package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import org.jetbrains.annotations.NotNull;

public interface DdtDescriptor {
    void started();

    void ended(@NotNull TestResult testTestResult);

    ConfigurableTestContext getContext();
}
