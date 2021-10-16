package com.github.thorbenkuck.ddt.selectors;

import com.github.thorbenkuck.ddt.descriptors.TestClassDescriptor;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.MethodSelector;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.thorbenkuck.ddt.DataDrivenTestEngine.IS_DATA_DRIVEN_TEST_METHOD;

public class DdtMethodSelectorProcessor implements DdtSelectorProcessor<MethodSelector> {

    @Override
    public List<TestDescriptor> process(UniqueId uniqueId, MethodSelector methodSelector) {
        ArrayList<TestDescriptor> testDescriptors = new ArrayList<>();
        Method javaMethod = methodSelector.getJavaMethod();

        if (!IS_DATA_DRIVEN_TEST_METHOD.test(javaMethod)) {
            return testDescriptors;
        }

        Class<?> javaClass = methodSelector.getJavaClass();
        TestClassDescriptor descriptor = TestClassDescriptor.build(uniqueId, javaClass, Collections.singletonList(javaMethod));
        testDescriptors.add(descriptor);

        return testDescriptors;
    }
}
