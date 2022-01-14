package com.github.thorbenkuck.ddt.spring;

import com.github.thorbenkuck.ddt.api.annotations.marker.ConsumesAnnotation;
import com.github.thorbenkuck.ddt.api.discovery.core.AbstractEngineCore;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.ExecutionContext;
import io.github.classgraph.ClassGraph;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringBootEngineCore extends AbstractEngineCore {

    private ConfigurableApplicationContext applicationContext;
    private boolean singleton = false;
    private final SpringApplicationContextFactory contextFactory = new SpringApplicationContextFactory();

    @ConsumesAnnotation
    public void consumeAnnotation(SpringBootDdtTest annotation) {
        System.out.println("Consuming SpringBootDdtTest Annotation");
        singleton = annotation.singleton();
    }

    @Override
    public void initialize(Class<?> clazz) {
        if(applicationContext == null) {
            applicationContext = contextFactory.createFor(clazz);
        }
    }

    @Override
    public boolean singleton() {
        return singleton;
    }

    @Override
    public <T> T createInstanceOf(Class<T> type) {
        return applicationContext.getBean(type);
    }

    @Override
    public void injectDependencies(Object o) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(o);
    }

    @Override
    public void tearDownTestInstance(@NotNull Object testInstance) {
        applicationContext.getAutowireCapableBeanFactory().destroyBean(testInstance);
    }
}
