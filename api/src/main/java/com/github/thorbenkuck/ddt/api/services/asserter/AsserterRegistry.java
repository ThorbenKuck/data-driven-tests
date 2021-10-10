package com.github.thorbenkuck.ddt.api.services.asserter;

import com.github.thorbenkuck.ddt.api.domain.Asserter;

import java.util.*;

public class AsserterRegistry {

    private final List<Asserter> asserters = new ArrayList<>();
    private boolean setup = false;

    public void register(Asserter asserter) {
        asserters.add(asserter);
    }

    public List<Asserter> access() {
        return Collections.unmodifiableList(asserters);
    }

    public boolean isSetup() {
        return setup;
    }

    public void setupDone() {
        setup = true;
    }
}
