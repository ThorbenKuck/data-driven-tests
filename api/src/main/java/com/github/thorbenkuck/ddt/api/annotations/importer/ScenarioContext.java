package com.github.thorbenkuck.ddt.api.annotations.importer;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.CollectedBy;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.ScenarioContextMetaData;
import com.github.thorbenkuck.ddt.api.discovery.core.type.EngineCore;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Inherited
@Documented
@CollectedBy(ScenarioContextMetaData.class)
public @interface ScenarioContext {
    Class<? extends EngineCore> value();
}
