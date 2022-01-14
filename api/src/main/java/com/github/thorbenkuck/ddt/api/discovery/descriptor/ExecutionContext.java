package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import com.github.thorbenkuck.ddt.api.annotations.TestScenarioValues;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import com.github.thorbenkuck.ddt.api.domain.listener.TestScenarioListener;
import com.github.thorbenkuck.ddt.api.domain.listener.TestScenarioListenerContext;
import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.reflect.Method;
import java.util.Optional;

public interface ExecutionContext {
    Class<?> getTestClass();

    Optional<Object> getTestInstance();

    Object requireTestInstance();

    Optional<Method> getTestMethod();

    Method requireTestMethod();

    Optional<TestScenarioValues> getAnnotationValues();

    TestScenarioValues requireAnnotationValues();

    Optional<TestCaseContent> getTestCaseContent();

    TestCaseContent requireTestCaseContent();

    EngineCore getEngineCore();

    OrderedSet<Asserter> getAsserters();

    TestScenarioListenerContext getListeners();

    boolean addAsserter(Asserter asserter);

    boolean addListener(TestScenarioListener listener);
}
