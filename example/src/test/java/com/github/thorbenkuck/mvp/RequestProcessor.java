package com.github.thorbenkuck.mvp;

class RequestProcessor {
    public DataContent process(DataContent dataContent) {
        if (dataContent.getContent().equalsIgnoreCase("foo")) {
            return new DataContent("bar");
        } else if (dataContent.getContent().equalsIgnoreCase("bar")) {
            return new DataContent("baz");
        } else {
            return new DataContent("qux");
        }
    }
}
