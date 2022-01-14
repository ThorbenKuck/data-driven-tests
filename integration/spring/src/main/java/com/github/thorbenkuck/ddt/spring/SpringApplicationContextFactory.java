package com.github.thorbenkuck.ddt.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringApplicationContextFactory {

    private final SpringApplicationFinder finder = new SpringApplicationFinder();

    public ConfigurableApplicationContext createFor(Class<?> type) {
        Class<?> mainClass = finder.findMainClass(type);

        return SpringApplication.run(mainClass);
    }

}
