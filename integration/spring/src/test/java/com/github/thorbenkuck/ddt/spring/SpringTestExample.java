package com.github.thorbenkuck.ddt.spring;

import com.github.thorbenkuck.ddt.api.annotations.importer.TestCaseFactories;
import com.github.thorbenkuck.ddt.api.annotations.marker.ApplyPreconditions;
import com.github.thorbenkuck.ddt.api.annotations.marker.ConfigureTestData;
import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;
import com.github.thorbenkuck.ddt.asserters.AssertJEquals;
import org.springframework.context.annotation.Profile;

@SpringBootDdtTest
public class SpringTestExample implements TestInterface {

    @ConfigureTestData
    public static void configureTestData(TestSuiteBuilder<String, String> builder) {
        builder.addEntry("From the test-class itself!")
                .forInput("Foo")
                .expect("Foo");
    }

    @ApplyPreconditions
    public void applyPreconditions(String s) {
        System.out.println("[PRECONDITION]: " + s);
    }

    @TestScenario(suite = "foo", factories = {TestExampleFactory.class})
    @TestCaseFactories(TestExampleFactory.class)
    @AssertJEquals
    public String test(String s) {
        return s;
    }
}
