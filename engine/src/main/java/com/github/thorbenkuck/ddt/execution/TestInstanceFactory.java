package com.github.thorbenkuck.ddt.execution;

import com.github.thorbenkuck.ddt.Reflections;
import com.github.thorbenkuck.ddt.descriptors.TestCaseDescriptor;
import com.github.thorbenkuck.ddt.execution.exception.InstanceCreationException;

public class TestInstanceFactory {

    public static Object createNewInstance(TestCaseDescriptor methodTestDescriptor) {
        Object testInstance;
        try {
            testInstance = Reflections.newInstance(methodTestDescriptor.getRoot());
        } catch (Throwable throwable) {
            String message = String.format( //
                    "Cannot create instance of class '%s'. Maybe it has no default constructor?", //
                    methodTestDescriptor.getRoot() //
            );
            throw new InstanceCreationException(message, throwable);
        }

        return testInstance;
    }
}
