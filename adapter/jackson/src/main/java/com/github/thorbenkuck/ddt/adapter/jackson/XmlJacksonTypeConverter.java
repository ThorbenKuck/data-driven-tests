package com.github.thorbenkuck.ddt.adapter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.thorbenkuck.ddt.api.domain.TypeConverterAdapter;
import com.google.auto.service.AutoService;

import java.io.IOException;

@AutoService(TypeConverterAdapter.class)
public class XmlJacksonTypeConverter implements TypeConverterAdapter {

    private final ObjectMapper mapper = new XmlMapper().findAndRegisterModules();

    @Override
    public <T> T convert(byte[] content, Class<T> targetType) {
        try {
            return mapper.readValue(content, targetType);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String supportedFileType() {
        return "json";
    }
}
