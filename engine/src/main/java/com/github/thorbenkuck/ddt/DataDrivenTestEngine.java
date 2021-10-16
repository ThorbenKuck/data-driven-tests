package com.github.thorbenkuck.ddt;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.selectors.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.*;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class DataDrivenTestEngine implements TestEngine {

    private static final String ID = "DataDriveTestEngine";

    public static final Predicate<Method> IS_DATA_DRIVEN_TEST_METHOD = method -> {
        if (!AnnotationSupport.isAnnotated(method, TestScenario.class)) {
            return false;
        }
        if (ReflectionUtils.isAbstract(method)) {
            return false;
        }
        if (ReflectionUtils.isPrivate(method)) {
            return false;
        }
        if (ReflectionUtils.isStatic(method)) {
            return false;
        }
        if (method.getParameterCount() == 0 || method.getParameterCount() > 2) {
            return false;
        }
        if (method.getReturnType().equals(Void.TYPE)) {
            return method.getParameterCount() == 2;
        } else {
            return method.getParameterCount() == 1;
        }
    };

    public static final Predicate<Class<?>> IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD = clazz -> {
        if (ReflectionUtils.isAbstract(clazz)) {
            return false;
        }
        if (ReflectionUtils.isPrivate(clazz)) {
            return false;
        }
        if (ReflectionUtils.isStatic(clazz)) {
            return false;
        }

        return Arrays.stream(clazz.getMethods())
                .anyMatch(IS_DATA_DRIVEN_TEST_METHOD);
    };

    private static final DdtSelectorProcessProcessor selectorProcess = new DdtSelectorProcessProcessor();

    static {
        selectorProcess.register(MethodSelector.class, new DdtMethodSelectorProcessor());
        selectorProcess.register(ClassSelector.class, new DdtClassSelectorProcessor());
        selectorProcess.register(PackageSelector.class, new DdtPackageSelectorProcessor());
        selectorProcess.register(ModuleSelector.class, new DdtModuleSelectorProcessor());
        selectorProcess.register(ClasspathRootSelector.class, new DdtClassPathRootSelectorProcessor());
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId engineId) {
        EngineDescriptor engineDescriptor = new EngineDescriptor(engineId, "Data Driven Tests Engine");

        List<TestDescriptor> descriptors = selectorProcess.processAll(discoveryRequest, engineId);
        descriptors.forEach(engineDescriptor::addChild);

        return engineDescriptor;
    }

    @Override
    public void execute(ExecutionRequest request) {
        TestDescriptor rootTestDescriptor = request.getRootTestDescriptor();
        try {
            new DataDrivenTestExecutor().execute(request, rootTestDescriptor);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
