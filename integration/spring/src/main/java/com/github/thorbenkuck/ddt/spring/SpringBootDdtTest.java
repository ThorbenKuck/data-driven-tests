package com.github.thorbenkuck.ddt.spring;

import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
@ScenarioContext(SpringBootEngineCore.class)
public @interface SpringBootDdtTest {

    /**
     * Application arguments that should be passed to the application under test.
     *
     * @return the application arguments to pass to the application under test.
     * @see ApplicationArguments
     * @see SpringApplication#run(String...)
     * @since 2.2.0
     */
    String[] args() default {};

    /**
     * The <em>component classes</em> to use for loading an
     * {@link org.springframework.context.ApplicationContext ApplicationContext}. Can also
     * be specified using
     * {@link ContextConfiguration#classes() @ContextConfiguration(classes=...)}. If no
     * explicit classes are defined the test will look for nested
     * {@link Configuration @Configuration} classes, before falling back to a
     * {@link SpringBootConfiguration @SpringBootConfiguration} search.
     *
     * @return the component classes used to load the application context
     * @see ContextConfiguration#classes()
     */
    Class<?>[] classes() default {};

    boolean singleton() default false;

}
