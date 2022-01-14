package com.github.thorbenkuck.ddt.api.annotations.marker;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestFailException {
}
