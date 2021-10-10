package com.github.thorbenkuck.ddt.api.annotations.lifecycle;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BeforeScenario {
}
