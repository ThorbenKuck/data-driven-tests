package com.github.thorbenkuck.ddt;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.asserters.AssertJEquals;

public class MVPTest {

    @TestScenario(suite = "example")
    @AssertJEquals
    public TestType test(TestType testType) {
        TestType testType1 = new TestType();

        testType1.setContent(testType.getContent() + 1);

        return testType1;
    }
}
