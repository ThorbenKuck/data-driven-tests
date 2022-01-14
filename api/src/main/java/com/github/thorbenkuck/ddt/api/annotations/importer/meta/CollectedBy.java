package com.github.thorbenkuck.ddt.api.annotations.importer.meta;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CollectedBy {

    Class<?> value();

}
