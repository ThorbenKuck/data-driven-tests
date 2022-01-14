package com.github.thorbenkuck.ddt.junit.selectors;

import com.github.thorbenkuck.ddt.api.discovery.TestDiscoverer;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.engine.discovery.ModuleSelector;

import java.util.List;

public class ModuleSelectorProcessor implements SelectorProcessor<ModuleSelector> {
    @Override
    public TestDiscoverer process(ModuleSelector selector) {
        List<Class<?>> allClassesInPackage = ReflectionSupport.findAllClassesInModule(
                selector.getModuleName(),
                clazz -> true,
                name -> true
        );

        return TestDiscoverer.initialize()
                .appendForSearch(allClassesInPackage);
    }
}
