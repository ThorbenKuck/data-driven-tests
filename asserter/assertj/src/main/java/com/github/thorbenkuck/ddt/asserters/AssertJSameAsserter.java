package com.github.thorbenkuck.ddt.asserters;

import com.github.thorbenkuck.ddt.api.domain.Asserter;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AssertJSameAsserter implements Asserter {

    @Override
    public void doAssert(Object expected, Object actual) throws AssertionFailedError {
        assertThat(actual).isSameAs(expected);
    }

    @Override
    public String name() {
        return "AssertJSame";
    }
}
