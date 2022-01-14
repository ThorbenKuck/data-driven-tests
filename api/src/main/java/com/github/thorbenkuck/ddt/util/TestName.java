package com.github.thorbenkuck.ddt.util;

public class TestName {

    public static String produceOf(String input) {
        String snakeCase = camelToSnake(input);
        return snakeCase.replaceAll("_", " ");
    }

    public static String camelToSnake(String input) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        return input.replaceAll(regex, replacement)
                .toLowerCase();
    }
}
