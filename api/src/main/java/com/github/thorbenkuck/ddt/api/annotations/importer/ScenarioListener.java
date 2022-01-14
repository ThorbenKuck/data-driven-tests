package com.github.thorbenkuck.ddt.api.annotations.importer;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.CollectedBy;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.TestCaseFactoryMetaData;
import com.github.thorbenkuck.ddt.api.domain.listener.ContextFreeScenarioListener;
import com.github.thorbenkuck.ddt.api.domain.listener.TestScenarioListener;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Documented
@CollectedBy(TestCaseFactoryMetaData.class)
public @interface ScenarioListener {

    Class<? extends TestScenarioListener>[] inContext() default {};

    Class<? extends ContextFreeScenarioListener>[] contextFree() default {};

}
