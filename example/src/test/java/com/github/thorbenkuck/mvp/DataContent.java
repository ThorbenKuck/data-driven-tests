package com.github.thorbenkuck.mvp;

import java.util.Objects;

class DataContent {
    private final String content;

    public DataContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataContent that = (DataContent) o;
        return Objects.equals(content, that.content);
    }
}
