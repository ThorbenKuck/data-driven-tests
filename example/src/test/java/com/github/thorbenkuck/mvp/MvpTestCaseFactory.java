package com.github.thorbenkuck.mvp;

import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.api.domain.builder.TestSuiteBuilder;

public class MvpTestCaseFactory implements TestCaseFactory<DataContent, DataContent> {

//    abstract fun TestSuiteBuilder<T, S>.build();
//
//    override fun TestSuiteBuilder<DataContent, DataContent>() {
//        addEntry("foo") {
//            forInput("Foo")
//                    .expect("Bar")
//        }
//
//        addEntry("Bar") {
//            forInput("Bar")
//                    .expect("Foo")
//        }
//    }

    @Override
    public void setup(TestSuiteBuilder<DataContent, DataContent> builder) {
        // Style of Writing 1
        builder.append(newEntry("Foo To Bar")
                .forInput(new DataContent("foo"))
                .expects(new DataContent("bar")));

        // Style of Writing 2
        builder.addEntry("Bar to Baz")
                .forInput(new DataContent("bar"))
                .expect(new DataContent("baz"));

        // Style of Writing 3 (especially for Kotlin)
        /**
         * kotlin like
         *
         * builder.addEntry("foo") {
         *     forInput(new DataContent("other"))
         *         .expect(new DataContent("qux"));
         * }
         */
        builder.addEntry("Other to Qux", consuming -> {
            consuming.forInput(new DataContent("other"))
                    .expect(new DataContent("qux"));
        });


        builder.append(newEntry("Empty to Qux")
                .forInput(new DataContent(""))
                .expects(new DataContent("qux")));
    }

    @Override
    public String suite() {
        return "mvp";
    }
}