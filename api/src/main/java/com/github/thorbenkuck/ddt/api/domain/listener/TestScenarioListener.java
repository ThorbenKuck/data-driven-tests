package com.github.thorbenkuck.ddt.api.domain.listener;

import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioListener;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.CollectedBy;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.TestCaseFactoryMetaData;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.ExecutionContext;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import org.jetbrains.annotations.NotNull;

@CollectedBy(TestCaseFactoryMetaData.class)
public interface TestScenarioListener extends Comparable<TestScenarioListener> {

    default void beforeClass(ExecutionContext testContext) {}
    default void beforeMethod(ExecutionContext testContext) {}
    default void beforeCase(ExecutionContext testContext) {}
    default void afterCase(ExecutionContext testContext, TestResult result) {}
    default void afterMethod(ExecutionContext testContext, TestResult result) {}
    default void afterClass(ExecutionContext testContext, TestResult result) {}

    @Override
    default int compareTo(@NotNull TestScenarioListener other) {
        return order() - other.order();
    }

    default int order() {
        return 0;
    }

    static OrderedSet<TestScenarioListener> findAll(Class<?> type, EngineCore engineCore) {
        return AnnotationCollector.of(ScenarioListener.class)
                .searchIn(type)
                .analyze()
                .flatMap(ScenarioListener::inContext)
                .<TestScenarioListener>map(engineCore::createListenerInstance)
                .asOrderedSet();
    }
}
