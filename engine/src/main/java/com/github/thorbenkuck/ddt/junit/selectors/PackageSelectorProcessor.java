package com.github.thorbenkuck.ddt.junit.selectors;

import com.github.thorbenkuck.ddt.api.discovery.TestDiscoverer;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.engine.discovery.PackageSelector;

import java.util.List;

public class PackageSelectorProcessor implements SelectorProcessor<PackageSelector> {
    @Override
    public TestDiscoverer process(PackageSelector selector) {
        List<Class<?>> allClassesInPackage = ReflectionSupport.findAllClassesInPackage(
                selector.getPackageName(),
                clazz -> true,
                name -> true
        );

        return TestDiscoverer.initialize()
                .appendForSearch(allClassesInPackage);
    }
}
