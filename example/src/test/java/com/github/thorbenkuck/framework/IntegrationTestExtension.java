package com.github.thorbenkuck.framework;

import com.github.thorbenkuck.domain.UserRepository;
import com.github.thorbenkuck.ddt.api.annotations.ConsumesAnnotation;
import com.github.thorbenkuck.ddt.api.domain.TestScenarioListener;
import org.junit.platform.engine.TestExecutionResult;

public class IntegrationTestExtension implements TestScenarioListener {

    private final ApplicationContext applicationContext = ApplicationContext.start();

    @Override
    public void beforeCase(Object testCaseInstance) {
        applicationContext.injectInto(testCaseInstance);
    }

    @Override
    public void afterCase(Object testInstance, TestExecutionResult result) {
        applicationContext.getBean(UserRepository.class).clear();
    }

    @ConsumesAnnotation
    public void accept(IntegrationTest integrationTest) {
    }
}
