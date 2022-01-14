package com.github.thorbenkuck.framework;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.ExecutionContext;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import com.github.thorbenkuck.ddt.api.domain.listener.TestScenarioListener;

public class ExampleScenarioListener implements TestScenarioListener {
    @Inject
    private RestTemplate restTemplate;

    @Override
    public void beforeMethod(ExecutionContext executionContext) {
        System.out.println("[Example]: Before Method: " + restTemplate);
    }

    @Override
    public void beforeCase(ExecutionContext testContext) {
        System.out.println("[Example]: Before Case: " + restTemplate);
    }

    @Override
    public void afterCase(ExecutionContext context, TestResult result) {
        System.out.println("[Example]: After Case: " + restTemplate);
    }

    @Override
    public void afterMethod(ExecutionContext context, TestResult result) {
        System.out.println("[Example]: After Method: " + restTemplate);
    }
}
