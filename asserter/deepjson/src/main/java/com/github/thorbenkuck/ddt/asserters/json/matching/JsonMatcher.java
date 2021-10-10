package com.github.thorbenkuck.ddt.asserters.json.matching;

import com.github.thorbenkuck.ddt.asserters.json.Assertions;
import com.github.thorbenkuck.ddt.asserters.json.IgnoreField;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JsonMatcher {

    private final Object actual;
    private JsonWriter jsonWriter = new SimpleJacksonJsonWriter();

    public JsonMatcher(Object actual) {
        this.actual = actual;
    }

    public JsonMatcher usingConverter(JsonWriter jsonWriter) {
        this.jsonWriter = jsonWriter;

        return this;
    }

    public JsonMatcher ignoringFields(IgnoreField... ignoreField) {
        return ignoringFields(Arrays.stream(ignoreField).map(annotation -> new IgnoredField(annotation.name(), annotation.reason(), annotation.temporary())).collect(Collectors.toList()));
    }

    public JsonMatcher ignoringFields(IgnoredField... ignoreField) {
        return ignoringFields(Arrays.asList(ignoreField));
    }

    public JsonMatcher ignoringFields(List<IgnoredField> ignoreField) {
        jsonWriter = new FieldIgnoringJacksonJsonWriter(ignoreField);
        return this;
    }

    public void matches(Object expected) {
        Assertions.deepJsonAssert(expected, actual, false, jsonWriter);
    }

    public void isSameAs(Object expected) {
        Assertions.deepJsonAssert(expected, actual, true, jsonWriter);
    }
}
