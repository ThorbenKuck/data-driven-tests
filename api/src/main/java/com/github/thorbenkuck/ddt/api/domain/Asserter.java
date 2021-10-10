package com.github.thorbenkuck.ddt.api.domain;

import org.opentest4j.AssertionFailedError;

public interface Asserter {

    void doAssert(Object expected, Object actual) throws AssertionFailedError;

    default String name() {
        return getClass().getSimpleName();
    }

}
