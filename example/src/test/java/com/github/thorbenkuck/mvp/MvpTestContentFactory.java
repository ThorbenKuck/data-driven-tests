package com.github.thorbenkuck.mvp;

import com.github.thorbenkuck.ddt.api.domain.TestContentFactory;
import com.github.thorbenkuck.ddt.api.domain.factory.TestCaseEntry;

import java.util.List;

public class MvpTestContentFactory implements TestContentFactory<DataContent, DataContent> {

    @Override
    public List<TestCaseEntry<DataContent, DataContent>> produce() {
        return builder()
                // Style of Writing 1
                .append(entry("Foo To Bar")
                        .forInput(new DataContent("foo"))
                        .expects(new DataContent("bar")))

                // Style of Writing 2
                .addEntry("Bar to Baz")
                .forInput(new DataContent("bar"))
                .expects(new DataContent("baz"))

                // Style of Writing 3 (especially for Kotlin)
                .addEntry("Other to Qux", builder -> {
                    builder.forInput(new DataContent("other"))
                            .expects(new DataContent("qux"));
                })

                .append(entry("Empty to Qux")
                        .forInput(new DataContent(""))
                        .expects(new DataContent("qux")))
                .build();
    }

    @Override
    public String suite() {
        return "mvp";
    }
}