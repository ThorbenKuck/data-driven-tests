package com.github.thorbenkuck.ddt.adapter.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.github.thorbenkuck.ddt.api.domain.TypeConverterAdapter;
import com.google.auto.service.AutoService;

import java.io.IOException;

@AutoService(TypeConverterAdapter.class)
public class JsonJacksonTypeConverter implements TypeConverterAdapter {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
            .findAndRegisterModules();

    @Override
    public <T> T convert(byte[] content, Class<T> targetType) {
        try {
            return objectMapper.readValue(content, targetType);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String supportedFileType() {
        return "json";
    }
}
