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

import static com.github.thorbenkuck.ddt.DataDrivenTestEngine.IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD;
import static com.github.thorbenkuck.ddt.DataDrivenTestEngine.IS_DATA_DRIVEN_TEST_METHOD;

public class DdtClassSelectorProcessor implements DdtSelectorProcessor<ClassSelector> {

    @Override
    public List<TestDescriptor> process(UniqueId uniqueId, ClassSelector methodSelector) {
        Class<?> javaClass = methodSelector.getJavaClass();
        if(!IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD.test(javaClass)) {
            return Collections.emptyList();
        }

        List<Method> collect = Arrays.stream(javaClass.getMethods())
                .filter(IS_DATA_DRIVEN_TEST_METHOD)
                .collect(Collectors.toList());

        if(collect.isEmpty()) {
            return Collections.emptyList();
        }

        return Collections.singletonList(TestClassDescriptor.build(uniqueId, javaClass, collect));
    }
}
