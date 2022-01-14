package com.github.thorbenkuck.ddt.api.discovery;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestClassDescriptor;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DiscoveryResult {

    private final List<TestClassDescriptor> result;
    private final Duration duration;

    public DiscoveryResult(List<TestClassDescriptor> result, Duration duration) {
        this.result = result;
        this.duration = duration;
    }

    public List<TestClassDescriptor> list() {
        return result;
    }

    public Duration duration() {
        return duration;
    }

    public Stream<TestClassDescriptor> stream() {
        return result.stream();
    }

    public void forEach(Consumer<TestClassDescriptor> consumer) {
        result.forEach(consumer);
    }
}
