package com.github.thorbenkuck.ddt.api.annotations;

import org.apiguardian.api.API;

import java.lang.annotation.*;

import static org.apiguardian.api.API.Status.STABLE;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@API(status = STABLE, since = "5.0")
public @interface TestCaseLifecycle {

    Lifecycle value() default Lifecycle.PER_METHOD;

    enum Lifecycle {
        PER_METHOD,
        PER_CASE;
    }
}
