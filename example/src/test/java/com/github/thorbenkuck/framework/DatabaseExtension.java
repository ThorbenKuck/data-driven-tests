package com.github.thorbenkuck.framework;

import com.github.thorbenkuck.ddt.api.discovery.descriptor.ExecutionContext;
import com.github.thorbenkuck.ddt.api.discovery.descriptor.TestResult;
import com.github.thorbenkuck.ddt.api.domain.listener.TestScenarioListener;

public class DatabaseExtension implements TestScenarioListener {

    @Override
    public void afterCase(ExecutionContext testContext, TestResult result) {
        System.out.println("[DB]: Resetting all tables");
        DAO.reset();
    }
}
