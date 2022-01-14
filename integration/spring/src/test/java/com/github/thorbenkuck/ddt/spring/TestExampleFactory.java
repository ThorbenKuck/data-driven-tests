package com.github.thorbenkuck.ddt.spring;

import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;
import org.springframework.stereotype.Component;

@Component
public class TestExampleFactory implements TestCaseFactory<String, String> {
    @Override
    public void setup(TestSuiteBuilder<String, String> builder) {
        builder.addEntry("foo")
                .withPrecondition("foo")
                .forInput("foo")
                .expect("foo");

        builder.addEntry("bar")
                .forInput("bar")
                .expect("bar");
    }

    @Override
    public String suite() {
        return "foo";
    }
}
