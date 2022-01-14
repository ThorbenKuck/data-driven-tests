package com.github.thorbenkuck.ddt.api.annotations.importer;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.CollectedBy;
import com.github.thorbenkuck.ddt.api.domain.TestCaseLifecycleType;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@CollectedBy(TestCaseLifecycleType.class)
public @interface ScenarioLifecycle {

    Lifecycle value() default Lifecycle.PER_METHOD;

    enum Lifecycle {
        PER_METHOD,
        PER_CASE;
    }
}
