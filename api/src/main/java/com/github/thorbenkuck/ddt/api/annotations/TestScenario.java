package com.github.thorbenkuck.ddt.api.annotations;

import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.SuiteType;
import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

@Testable
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TestScenario {

    String suite();

    SuiteType suiteType() default SuiteType.ANY;

    String expectedFlag() default "expected";

    String name() default DEFAULT_NAME;

    Class<? extends Asserter>[] asserters() default {};

    String DEFAULT_NAME = "[{index}] {name}";
}

