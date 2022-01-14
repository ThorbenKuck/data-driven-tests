package com.github.thorbenkuck.ddt.asserters;

import com.github.thorbenkuck.ddt.api.annotations.importer.AssertedBy;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AssertedBy({AssertJEqualsAsserter.class})
@Inherited
public @interface AssertJEquals {
}
