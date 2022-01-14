package com.github.thorbenkuck.ddt.api.annotations.marker;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface ConfigureTestData {

    String[] suits() default {};

}
