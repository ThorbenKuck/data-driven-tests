package com.github.thorbenkuck.ddt.asserters;

import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.google.auto.service.AutoService;
import org.opentest4j.AssertionFailedError;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoService(Asserter.class)
public class AssertJEqualsAsserter implements Asserter {

    @Override
    public void doAssert(Object expected, Object actual) throws AssertionFailedError {
        assertThat(actual).isEqualTo(expected);
    }

    @Override
    public String name() {
        return "AssertJEquals";
    }
}
