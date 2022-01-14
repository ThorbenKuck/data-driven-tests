package com.github.thorbenkuck.ddt.junit;

import com.github.thorbenkuck.ddt.junit.selectors.*;
import org.junit.platform.engine.*;
import org.junit.platform.engine.discovery.*;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

import java.util.List;

public class DataDrivenTestEngine implements TestEngine {

    private static final String ID = "DataDriveTestEngine";

    private static final DdtSelectorProcessProcessor selectorProcess = new DdtSelectorProcessProcessor();

    static {
        selectorProcess.register(MethodSelector.class, new MethodSelectorProcessor());
        selectorProcess.register(ClassSelector.class, new ClassSelectorProcessor());
        selectorProcess.register(PackageSelector.class, new PackageSelectorProcessor());
        selectorProcess.register(ModuleSelector.class, new ModuleSelectorProcessor());
        selectorProcess.register(ClasspathRootSelector.class, new ClassPathRootSelectorProcessor());
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
