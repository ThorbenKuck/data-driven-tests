package com.github.thorbenkuck.ddt.selectors;

import com.github.thorbenkuck.ddt.descriptors.TestClassDescriptor;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.ClassSelector;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClassSelectorProcessor implements SelectorProcessor<ClassSelector> {

    @Override
    public List<TestDescriptor> process(UniqueId uniqueId, ClassSelector methodSelector) {
        Class<?> javaClass = methodSelector.getJavaClass();
        if (!Identification.containsTestScenario(javaClass)) {
            return Collections.emptyList();
        }

        List<Method> collect = Arrays.stream(javaClass.getMethods())
                .filter(Identification.isTestScenario())
                .collect(Collectors.toList());

        if (collect.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(TestClassDescriptor.build(uniqueId, javaClass, collect));
    }
}
