package com.github.thorbenkuck.ddt.api.annotations;

import com.github.thorbenkuck.ddt.api.domain.Asserter;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface AssertedBy {

    Class<? extends Asserter>[] value();

}
