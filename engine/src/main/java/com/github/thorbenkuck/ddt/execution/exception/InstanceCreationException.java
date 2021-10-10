package com.github.thorbenkuck.ddt.execution.exception;

public class InstanceCreationException extends RuntimeException {
    public InstanceCreationException(String message) {
        super(message);
    }

    public InstanceCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
