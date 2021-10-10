package com.github.thorbenkuck.ddt;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.descriptors.TestClassDescriptor;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.support.ReflectionSupport;
import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.*;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DataDrivenTestEngine implements TestEngine {

    private static final String ID = "DataDriveTestEngine";

    private static final Predicate<Method> IS_DATA_DRIVEN_TEST_METHOD = method -> {
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

    private static final Predicate<Class<?>> IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD = clazz -> {
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

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId engineId) {
        EngineDescriptor engineDescriptor = new EngineDescriptor(engineId, "Data Driven Tests Engine");

        // TODO Make me beautiful :(
        handleStream(discoveryRequest.getSelectorsByType(MethodSelector.class)
                .stream()
                .map(MethodSelector::getJavaClass), engineId, engineDescriptor);

        handleStream(discoveryRequest.getSelectorsByType(ClassSelector.class)
                .stream()
                .map(ClassSelector::getJavaClass), engineId, engineDescriptor);

        handleStream(discoveryRequest.getSelectorsByType(PackageSelector.class)
                .stream()
                .flatMap(packageSelector -> ReflectionSupport.findAllClassesInPackage(
                        packageSelector.getPackageName(),
                        IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD,
                        name -> true
                ).stream()), engineId, engineDescriptor);

        handleStream(discoveryRequest.getSelectorsByType(ModuleSelector.class)
                .stream()
                .flatMap(packageSelector -> ReflectionSupport.findAllClassesInModule(
                        packageSelector.getModuleName(),
                        IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD,
                        name -> true
                ).stream()), engineId, engineDescriptor);

        handleStream(discoveryRequest.getSelectorsByType(ClasspathRootSelector.class)
                .stream()
                .flatMap(packageSelector -> ReflectionSupport.findAllClassesInClasspathRoot(
                        packageSelector.getClasspathRoot(),
                        IS_CLASS_WITH_DATA_DRIVEN_TEST_METHOD,
                        name -> true
                ).stream()), engineId, engineDescriptor);
        return engineDescriptor;
    }

    private void handleStream(Stream<Class<?>> stream, UniqueId engineId, EngineDescriptor engineDescriptor) {
        stream.distinct().forEach(discoveryPair -> append(engineId, discoveryPair, engineDescriptor));
    }

    private void append(UniqueId engineId, Class<?> clazz, EngineDescriptor engineDescriptor) {
        TestClassDescriptor classDescriptor = new TestClassDescriptor(
                engineId,
                clazz
        );
        engineDescriptor.addChild(classDescriptor.resolve());
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
