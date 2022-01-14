package com.github.thorbenkuck;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.annotations.importer.TestCaseFactories;
import com.github.thorbenkuck.ddt.asserters.AssertJEquals;
import com.github.thorbenkuck.preconditions.ExampleTestCaseFactoryWithPrecondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@TestScenario
@TestCaseFactories(ExampleTestCaseFactoryWithPrecondition.class)
@AssertJEquals
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginTestScenario {
}
