package com.github.thorbenkuck.ddt.api.annotations.importer;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.CollectedBy;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.TestCaseFactoryMetaData;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
@CollectedBy(TestCaseFactoryMetaData.class)
public @interface TestContentFactory {

    Class<? extends TestCaseFactory<?, ?>> value();

}
