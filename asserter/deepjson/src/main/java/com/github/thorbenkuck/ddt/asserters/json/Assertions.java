package com.github.thorbenkuck.ddt.asserters.json;

import com.github.thorbenkuck.ddt.asserters.json.matching.JsonMatcher;
import com.github.thorbenkuck.ddt.asserters.json.matching.JsonWriter;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.jupiter.api.Assertions.fail;

public class Assertions {

    public static JsonMatcher assertThatJsonOf(Object actual) {
        return new JsonMatcher(actual);
    }

    public static void deepJsonAssert(Object expected, Object actual, boolean strict, JsonWriter jsonWriter) {
        if (expected == actual) {
            return;
        }

        if (expected == null || actual == null) {
            fail(() -> "expected " + expected + " but got " + actual);
        }

        String expectedJson = jsonWriter.write(expected);
        String actualJson = jsonWriter.write(actual);
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, strict);
        } catch (JSONException e) {
            throw new AssertionError("Error while parsing json. Is the JsonWriter correct?", e);
        }
    }
}
