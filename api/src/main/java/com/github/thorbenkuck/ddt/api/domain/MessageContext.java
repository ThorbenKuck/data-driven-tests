package com.github.thorbenkuck.ddt.api.domain;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MessageContext {

    private final @NotNull String template;
    private final Map<String, Object> variables = new HashMap<>();

    public MessageContext(@NotNull String template) {
        this.template = template;
        variables.put("ls", System.lineSeparator());
    }

    public void addVariable(String key, Object value) {
        variables.put(key, value);
    }

    public String build(Map<String, Object> additionalVariables) {
        HashMap<String, Object> variableContext = new HashMap<>(additionalVariables);
        variables.forEach(variableContext::putIfAbsent);
        StringBuilder templateBuilder = new StringBuilder(template);

        variableContext.forEach((key, value) -> replaceAll(templateBuilder, "{{" + key + "}}", value.toString()));

        return templateBuilder.toString();
    }

    public String build() {
        StringBuilder templateBuilder = new StringBuilder(template);
        variables.forEach((key, value) -> replaceAll(templateBuilder, "{{" + key + "}}", value.toString()));
        return templateBuilder.toString();
    }

    public static void replaceAll(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = builder.indexOf(from, index);
        }
    }
}
