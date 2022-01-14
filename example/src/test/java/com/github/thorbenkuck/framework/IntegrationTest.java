package com.github.thorbenkuck.framework;

import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioListener;
import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioContext;
import com.github.thorbenkuck.ddt.api.annotations.importer.ScenarioLifecycle;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@ScenarioContext(FrameworkEngineCore.class)
@ScenarioListener(inContext = {ExampleScenarioListener.class, DatabaseExtension.class})
@ScenarioLifecycle(ScenarioLifecycle.Lifecycle.PER_CASE)
public @interface IntegrationTest {

    boolean singleton() default false;

}
