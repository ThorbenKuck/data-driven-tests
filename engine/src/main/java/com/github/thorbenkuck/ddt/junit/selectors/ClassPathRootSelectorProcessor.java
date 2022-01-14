package com.github.thorbenkuck.ddt.junit.selectors;

import com.github.thorbenkuck.ddt.api.discovery.TestDiscoverer;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.engine.discovery.ClasspathRootSelector;

import java.util.List;


public class ClassPathRootSelectorProcessor implements SelectorProcessor<ClasspathRootSelector> {
    @Override
    public TestDiscoverer process(ClasspathRootSelector selector) {
        List<Class<?>> allClassesInPackage = ReflectionSupport.findAllClassesInClasspathRoot(
                selector.getClasspathRoot(),
                aClass -> true,
                name -> true
        );

        return TestDiscoverer.initialize()
                .appendForSearch(allClassesInPackage);
    }
}
