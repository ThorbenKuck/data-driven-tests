package com.github.thorbenkuck.ddt.api.discovery;

import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestClassDescriptor;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestMethodDescriptor;
import com.github.thorbenkuck.ddt.api.domain.StopWatch;
import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestDiscoverer {

    private final OrderedSet<Class<?>> prepareFor;
    private final OrderedSet<Method> methods;

    private TestDiscoverer(OrderedSet<Class<?>> prepareFor, OrderedSet<Method> methods) {
        // Instantiation only through static method
        this.prepareFor = prepareFor;
        this.methods = methods;
    }

    private static final TestDiscoverer EMPTY = new TestDiscoverer(OrderedSet.empty(), OrderedSet.empty());

    public static TestDiscoverer initialize() {
        return new TestDiscoverer(OrderedSet.create(), OrderedSet.create());
    }

    public static TestDiscoverer empty() {
        return EMPTY;
    }

    public TestDiscoverer joinWith(TestDiscoverer discoverer) {
        OrderedSet<Class<?>> newPrepareFor = OrderedSet.of(prepareFor).append(discoverer.prepareFor);
        OrderedSet<Method> newMethods = OrderedSet.of(methods).append(discoverer.methods);

        return new TestDiscoverer(newPrepareFor, newMethods);
    }

    public TestDiscoverer expandWith(TestDiscoverer testDiscoverer) {
        prepareFor.addAll(testDiscoverer.prepareFor);
        methods.addAll(testDiscoverer.methods);

        return this;
    }

    public TestDiscoverer appendForSearch(Class<?> root) {
        prepareFor.add(root);
        return this;
    }

    public TestDiscoverer appendForSearch(Collection<Class<?>> roots) {
        prepareFor.addAll(roots);
        return this;
    }

    public TestDiscoverer appendForSearch(Method method) {
        methods.add(method);
        return this;
    }

    public DiscoveryResult findAll() {
        final List<Class<?>> alreadyProcessed = new ArrayList<>();
        final List<TestClassDescriptor> result = new ArrayList<>();
        final StopWatch stopWatch = StopWatch.open();

        prepareFor.forEach(clazz -> {
            EngineCore engineCore = EngineCore.of(clazz);
            TestClassDescriptor.resolveWithChildren(engineCore, clazz).ifPresent(result::add);
            alreadyProcessed.add(clazz);
        });

        methods.stream()
                .filter(it -> !alreadyProcessed.contains(it.getDeclaringClass()))
                .map(method -> {
                    Class<?> declaringClass = method.getDeclaringClass();
                    EngineCore engineCore = EngineCore.of(declaringClass);

                    TestMethodDescriptor testMethodDescriptor = TestMethodDescriptor.resolveWithChildren(engineCore, method, declaringClass);
                    testMethodDescriptor.resolveChildren();
                    return testMethodDescriptor.resolveParent();
                }).forEach(result::add);

        Duration stop = stopWatch.stop();
        alreadyProcessed.clear();

        return new DiscoveryResult(result, stop);
    }

    public void clear() {
        prepareFor.clear();
        methods.clear();
    }
}
