package com.github.thorbenkuck.ddt.spring;

public class SpringTestApplicationContextConfiguration {

    private final Class<?> testClass;

    public SpringTestApplicationContextConfiguration(Class<?> testClass) {
        this.testClass = testClass;
    }
}
