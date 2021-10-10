package com.github.thorbenkuck.mvp;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;

public class MVPTest {

    @TestScenario(suite = "mvp")
    public DataContent test(DataContent request) {
        return new RequestProcessor().process(request);
    }
}
