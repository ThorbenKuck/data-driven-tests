package com.github.thorbenkuck.ddt.asserters;

import com.github.thorbenkuck.ddt.api.annotations.AssertedBy;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AssertedBy({AssertJSameAsserter.class})
@Inherited
public @interface AssertJSame {
}
