package com.github.thorbenkuck.ddt.selectors;

import com.github.thorbenkuck.ddt.descriptors.TestClassDescriptor;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.PackageSelector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.thorbenkuck.ddt.DataDrivenTestEngine.IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD;
import static com.github.thorbenkuck.ddt.DataDrivenTestEngine.IS_DATA_DRIVEN_TEST_METHOD;

public class DdtPackageSelectorProcessor implements DdtSelectorProcessor<PackageSelector> {
    @Override
    public List<TestDescriptor> process(UniqueId uniqueId, PackageSelector selector) {
        List<Class<?>> allClassesInPackage = ReflectionSupport.findAllClassesInPackage(
                selector.getPackageName(),
                IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD,
                name -> true
        );
        if (allClassesInPackage.isEmpty()) {
            return Collections.emptyList();
        }

        return allClassesInPackage.stream()
                .map(clazz -> toDescriptor(uniqueId, clazz))
                .collect(Collectors.toList());
    }

    private TestClassDescriptor toDescriptor(UniqueId uniqueId, Class<?> clazz) {
        return TestClassDescriptor.build(uniqueId, clazz);
    }
}
