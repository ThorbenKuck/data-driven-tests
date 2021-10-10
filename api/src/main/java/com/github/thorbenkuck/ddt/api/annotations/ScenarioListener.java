package com.github.thorbenkuck.ddt.api.annotations;

import com.github.thorbenkuck.ddt.api.domain.TestScenarioListener;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface ScenarioListener {

    Class<? extends TestScenarioListener>[] value();

}
