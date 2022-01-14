package com.github.thorbenkuck.ddt.api.annotations;

import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.SuiteType;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.*;

@Testable
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TestScenario {

    String suite() default DEFAULT_SUITE_NAME;

    SuiteType suiteType() default SuiteType.ANY;

    String expectedFileFlag() default DEFAULT_EXPECTED_FILE_FLAG;

    String name() default DEFAULT_NAME;

    Class<? extends Asserter>[] asserters() default {};

    Class<? extends TestCaseFactory<?, ?>>[] factories() default {};

    // ### Default values ###

    String DEFAULT_NAME = "[{index}] {name}";

    String DEFAULT_EXPECTED_FILE_FLAG = "expected";

    String DEFAULT_SUITE_NAME = "default";

    SuiteType DEFAULT_SUITE_TYPE = SuiteType.ANY;
}

