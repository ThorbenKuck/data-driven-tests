package com.github.thorbenkuck.ddt.asserters.json;

public @interface IgnoreField {

    String name();

    String reason() default "";

    boolean temporary() default false;

}
