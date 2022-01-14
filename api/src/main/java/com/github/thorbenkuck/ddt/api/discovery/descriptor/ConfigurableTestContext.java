package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import com.github.thorbenkuck.ddt.api.annotations.TestScenarioValues;
import com.github.thorbenkuck.ddt.api.annotations.marker.TestInstanceTrigger;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.listener.TestScenarioListener;
import com.github.thorbenkuck.ddt.api.domain.listener.TestScenarioListenerContext;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.properties.DdtProperties;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Method;
import java.util.Optional;

public class ConfigurableTestContext implements ExecutionContext {

    @NotNull
    private final Class<?> testClass;
    @NotNull
    private final EngineCore engineCore;
    @NotNull
    private final OrderedSet<Asserter> asserters = OrderedSet.create();
    @NotNull
    private final TestMethodExecutor testMethodExecutor;
    @NotNull
    private final TestInstance.Lifecycle testInstanceLifecycle;
    @NotNull
    private final TestScenarioListenerContext testScenarioListenerContext;

    @Nullable
    private Method method;
    @Nullable
    private TestScenarioValues annotationValues;
    @Nullable
    private Object testInstance;
    @Nullable
    private TestCaseContent testCaseContent;

    public ConfigurableTestContext(@NotNull Class<?> testClass, @NotNull EngineCore engineCore) {
        this.testClass = testClass;
        this.engineCore = engineCore;
        this.testScenarioListenerContext = new TestScenarioListenerContext(this);
        this.testMethodExecutor = new TestMethodExecutor(this);
        this.testInstanceLifecycle = AnnotationCollector.findFirst(TestInstance.class, testClass)
                .map(TestInstance::value)
                .orElseGet(DdtProperties::defaultTestInstanceType);
    }

    @Override
    @NotNull
    public Class<?> getTestClass() {
        return testClass;
    }



    @Override
    @NotNull
    public Optional<Object> getTestInstance() {
        return Optional.ofNullable(testInstance);
    }

    @Override
    @NotNull
    public Object requireTestInstance() {
        return getTestInstance().orElseThrow(() -> notPresent("test class instance"));
    }

    public void tryCreateTestInstance(@NotNull TestInstanceTrigger trigger) {
        if(trigger.triggersFor(testInstanceLifecycle)) {
            Object testInstance = engineCore.createTestInstance(testClass);
            setTestInstance(testInstance);
        }
    }

    public void tryTearDownTestInstance(@NotNull TestInstanceTrigger trigger)  {
        if(trigger.triggersFor(testInstanceLifecycle)) {
            if(testInstance != null) {
                engineCore.tearDownTestInstance(testInstance);
                setTestInstance(null);
            }
        }
    }

    public void setTestInstance(@Nullable Object instance) {
        this.testInstance = instance;
    }



    @Override
    @NotNull
    public Optional<Method> getTestMethod() {
        return Optional.ofNullable(method);
    }

    @Override
    @NotNull
    public Method requireTestMethod() {
        return getTestMethod().orElseThrow(() -> notPresent("test method"));
    }

    public void setTestMethod(@Nullable Method method) {
        asserters.clear();

        this.method = method;
        if(method != null) {
            this.annotationValues = TestScenarioValues.determine(method);
            AsserterDescriptor.findAll(annotationValues, testClass, method, engineCore)
                    .sinkInto(asserters);
        } else {
            this.annotationValues = null;
        }
    }

    @Override
    @NotNull
    public Optional<TestScenarioValues> getAnnotationValues() {
        return Optional.ofNullable(annotationValues);
    }

    @Override
    @NotNull
    public TestScenarioValues requireAnnotationValues() {
        return getAnnotationValues().orElseThrow(() -> notPresent("annotation values"));
    }



    @Override
    @NotNull
    public Optional<TestCaseContent> getTestCaseContent() {
        return Optional.ofNullable(testCaseContent);
    }

    @Override
    @NotNull
    public TestCaseContent requireTestCaseContent() {
        return getTestCaseContent().orElseThrow(() -> notPresent("TestCaseContent"));
    }

    public void setTestCaseContent(@Nullable TestCaseContent testCaseContent) {
        this.testCaseContent = testCaseContent;
    }



    @Override
    @NotNull
    public EngineCore getEngineCore() {
        return engineCore;
    }

    @Override
    @NotNull
    public OrderedSet<Asserter> getAsserters() {
        return asserters;
    }

    @Override
    @NotNull
    public TestScenarioListenerContext getListeners() {
        return testScenarioListenerContext;
    }

    @Override
    public boolean addAsserter(@NotNull Asserter asserter) {
        return asserters.add(asserter);
    }

    @Override
    public boolean addListener(@NotNull TestScenarioListener listener) {
        return testScenarioListenerContext.addListener(listener);
    }

    private IllegalStateException notPresent(@NotNull String name) {
        return new IllegalStateException("No " + name + " set");
    }

    @NotNull
    public TestMethodExecutor getTestMethodExecutor() {
        return testMethodExecutor;
    }
}
