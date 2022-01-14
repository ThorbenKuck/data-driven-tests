package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.annotations.marker.ConsumesAnnotation;

public interface ConsumeAnnotation<T> {

    @ConsumesAnnotation
    void consume(T annotation);

}
