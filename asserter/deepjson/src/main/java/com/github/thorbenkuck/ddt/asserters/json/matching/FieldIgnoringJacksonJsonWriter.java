package com.github.thorbenkuck.ddt.asserters.json.matching;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FieldIgnoringJacksonJsonWriter implements JsonWriter {

    private final List<IgnoredField> ignoredFieldList;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
            .findAndRegisterModules();

    public FieldIgnoringJacksonJsonWriter(List<IgnoredField> ignoredFieldList) {
        this.ignoredFieldList = new ArrayList<>(ignoredFieldList);
    }

    @Override
    public String write(Object input) {
        ObjectNode objectNode = objectMapper.valueToTree(input);

        ignoredFieldList.stream()
                .map(IgnoredField::getName)
                .forEach(name -> {
                    String[] entries = name.split("\\.");
                    removeRecursively(objectNode, new ArrayList<>(Arrays.asList(entries)));
                });

        try {
            return objectMapper.writeValueAsString(objectNode);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private void removeRecursively(ObjectNode objectNode, List<String> fieldsInOrder) {
        String currentEntry = fieldsInOrder.remove(0);
        if(fieldsInOrder.isEmpty()) {
            if(objectNode.has(currentEntry)) {
                objectNode.remove(currentEntry);
            }
        } else {
            if(objectNode.has(currentEntry)) {
                JsonNode next = objectNode.get(currentEntry);
                if(next.isObject()) {
                    removeRecursively((ObjectNode) next, fieldsInOrder);
                } else if(next.isArray()) {
                    next.forEach(node -> {
                        if(node.isObject()) {
                            removeRecursively((ObjectNode) node, fieldsInOrder);
                        }
                    });
                }
            }
        }
    }
}
