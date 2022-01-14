package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;
import com.github.thorbenkuck.ddt.api.domain.TestCaseContent;
import org.jetbrains.annotations.NotNull;

public class TestCaseDescriptor implements DdtDescriptor {

    private final int index;
    private final TestCaseContent testCaseContent;
    private final ConfigurableTestContext context;
    private final TestMethodDescriptor parent;
    private String displayName;

    protected TestCaseDescriptor(
            int index,
            TestCaseContent testCaseContent,
            TestMethodDescriptor parent,
            ConfigurableTestContext context
    ) {
        this.index = index;
        this.testCaseContent = testCaseContent;
        this.parent = parent;
        this.context = context;
        this.displayName = displayName = "[" + index + "] " + testCaseContent.rawName();
    }

    public <T extends EngineCore> T getTestCaseContent() {
        return (T) this.testCaseContent;
    }

    public String suggestedDisplayName() {
        return displayName;
    }

    public void suggestNewDisplayName(String name) {
        this.displayName = name;
    }

    public TestMethodDescriptor getParent() {
        return parent;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void started() {
        context.setTestCaseContent(testCaseContent);
        context.getListeners().beforeCase();
    }

    @Override
    public void ended(@NotNull TestResult testTestResult) {
        context.getListeners().afterCase(testTestResult);
        context.setTestCaseContent(null);
    }

    @Override
    public ConfigurableTestContext getContext() {
        return context;
    }

    public TestResult run() {
        try {
            context.getTestMethodExecutor().applyPreconditions();
            return context.getTestMethodExecutor()
                    .invokeTestMethod(suggestedDisplayName());
        } catch (Throwable e) {
            return TestResult.of(e);
        }
    }
}
