package com.github.thorbenkuck.ddt.api.annotations.importer;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.AsserterMetaData;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.CollectedBy;
import com.github.thorbenkuck.ddt.api.domain.Asserter;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@CollectedBy(AsserterMetaData.class)
public @interface AssertedBy {

    Class<? extends Asserter>[] value();

}
