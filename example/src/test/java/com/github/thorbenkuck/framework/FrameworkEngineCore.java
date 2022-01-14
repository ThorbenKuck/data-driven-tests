package com.github.thorbenkuck.framework;

import com.github.thorbenkuck.ddt.api.annotations.marker.ConsumesAnnotation;
import com.github.thorbenkuck.ddt.api.domain.ConsumeAnnotation;
import com.github.thorbenkuck.ddt.api.discovery.core.DefaultEngineCore;

public class FrameworkEngineCore extends DefaultEngineCore implements ConsumeAnnotation<IntegrationTest> {

    private final ApplicationContext applicationContext = ApplicationContext.start();
    private boolean singleton = false;

    public FrameworkEngineCore() {
        System.out.println("Created the Framework-Test Scenario Context");
    }

    @Override
    public void consume(IntegrationTest test) {
        System.out.println("[METHOD]: CONSUMING IntegrationTest");
        singleton = test.singleton();
    }

    @ConsumesAnnotation
    public void generalConsume(IntegrationTest test) {
        System.out.println("[ANNOTATION]: CONSUMING IntegrationTest");
        singleton = test.singleton();
    }

    @Override
    public void injectDependencies(Object o) {
//        System.out.println("Injecting Dependencies into " + o.getClass().getSimpleName());
        applicationContext.injectInto(o);
    }

    @Override
    public <T> T createInstanceOf(Class<T> scenarioType) {
        T t = super.createInstanceOf(scenarioType);
        injectDependencies(t);
        return t;
    }

    @Override
    public <T> T createTestInstance(Class<T> test) {
        return createInstanceOf(test);
    }

    @Override
    public boolean singleton() {
        return singleton;
    }
}
