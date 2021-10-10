package com.github.thorbenkuck.ddt;

import java.util.Objects;

public class TestType {

    private int content;

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TestType{" +
                "content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestType testType = (TestType) o;
        return Objects.equals(content, testType.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
