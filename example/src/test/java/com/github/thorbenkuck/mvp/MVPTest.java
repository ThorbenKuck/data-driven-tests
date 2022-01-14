package com.github.thorbenkuck.mvp;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;

public class MVPTest {

    private final RequestProcessor testSubject = new RequestProcessor();

    @TestScenario(suite = "mvp")
    public DataContent test(DataContent request) {
        return testSubject.process(request);
    }
}
