package com.github.thorbenkuck.ddt;

import java.io.IOException;

class PropertySyntaxError extends Error {
    public PropertySyntaxError(String propertyNames, IOException ioException) {
        super("The properties " + propertyNames + " could not be parsed correctly. Please make sure, that the Properties are in a correct format!", ioException);
    }
}