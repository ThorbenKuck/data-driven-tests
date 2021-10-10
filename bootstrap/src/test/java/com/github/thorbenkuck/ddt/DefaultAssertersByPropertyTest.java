package com.github.thorbenkuck.ddt;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;

public class DefaultAssertersByPropertyTest {

    static {
        System.setProperty("ddt.asserters.fallback", "com.github.thorbenkuck.test.data.api.prepared.AssertJEqualsAsserter,com.github.thorbenkuck.test.data.api.prepared.AssertJEqualsAsserter");
    }

    @TestScenario(suite = "example")
    public TestType test(TestType testType) {
        TestType testType1 = new TestType();

        testType1.setContent(testType.getContent() + 1);

        return testType1;
    }
}
