package com.github.thorbenkuck.ddt.api.domain;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.AsserterMetaData;
import com.github.thorbenkuck.ddt.api.annotations.importer.meta.CollectedBy;
import org.opentest4j.AssertionFailedError;

@CollectedBy(AsserterMetaData.class)
public interface Asserter {

    void doAssert(Object expected, Object actual) throws AssertionFailedError;

    default String name() {
        return getClass().getSimpleName();
    }

}
