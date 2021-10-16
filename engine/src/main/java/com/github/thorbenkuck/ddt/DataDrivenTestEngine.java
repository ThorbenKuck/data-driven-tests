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
