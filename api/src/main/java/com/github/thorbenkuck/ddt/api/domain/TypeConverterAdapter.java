package com.github.thorbenkuck.ddt.api.domain;

public interface TypeConverterAdapter {

    <T> T convert(byte[] content, Class<T> targetType);

    String supportedFileType();

    default int order() {
        return DEFAULT_PRECEDENCE;
    }

    int DEFAULT_PRECEDENCE = 0;
    int HIGHEST_PRECEDENCE = Integer.MAX_VALUE;
    int LOWEST_PRECEDENCE = Integer.MIN_VALUE;
}
