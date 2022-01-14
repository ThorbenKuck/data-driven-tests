package com.github.thorbenkuck.ddt.properties;

import java.io.IOException;

/**
 * This Error is package-private only. No way of Catching it specifically.
 *
 * If this exception is raised, there is no way of recovery. It means that the ddt.properties (or any specific
 * mapped name), exists, but the parsing failed.
 */
class PropertySyntaxError extends Error {
    PropertySyntaxError(String propertyNames, IOException ioException) {
        super("The properties " + propertyNames + " could not be parsed correctly. Please make sure, that the Properties are in a correct format!", ioException);
    }
}