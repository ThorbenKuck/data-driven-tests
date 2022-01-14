package com.github.thorbenkuck.ddt.spring;

import com.github.thorbenkuck.ddt.api.annotations.marker.ConsumesAnnotation;

public interface TestInterface {
    @ConsumesAnnotation
    default void consume(SpringBootDdtTest annotation) {
        System.out.println("Hey");
    }
}
