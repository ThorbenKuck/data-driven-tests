package com.github.thorbenkuck.ddt.asserters.json;

import com.github.thorbenkuck.ddt.api.annotations.ConsumesAnnotation;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeepJsonEqualsAsserter implements Asserter {

    private final List<IgnoreField> ignoredFields = new ArrayList<>();

    @ConsumesAnnotation
    public void consumeAnnotation(DeepJsonEquals deepJsonEquals) {
        Collections.addAll(ignoredFields, deepJsonEquals.ignoreFields());
    }

    @Override
    public void doAssert(Object expected, Object actual) throws AssertionFailedError {

    }

    @Override
    public String name() {
        return "DeepJsonEquals";
    }
}
