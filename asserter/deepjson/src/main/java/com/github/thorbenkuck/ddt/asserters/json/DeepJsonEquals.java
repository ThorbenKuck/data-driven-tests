package com.github.thorbenkuck.ddt.asserters.json;

import com.github.thorbenkuck.ddt.api.annotations.importer.AssertedBy;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AssertedBy({DeepJsonEqualsAsserter.class})
@Inherited
public @interface DeepJsonEquals {

    IgnoreField[] ignoreFields() default {};

}
