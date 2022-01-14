package com.github.thorbenkuck.ddt.api.annotations.marker;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ApplyPreconditions {
}
