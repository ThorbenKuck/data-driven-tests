package com.github.thorbenkuck.framework;

import com.github.thorbenkuck.ddt.api.annotations.ScenarioListener;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@ScenarioListener(IntegrationTestExtension.class)
public @interface IntegrationTest {
}
