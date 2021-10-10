package com.github.thorbenkuck.ddt.api.services.adapter;

import com.github.thorbenkuck.ddt.api.domain.TypeConverterAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TypeConverterAdapterRegistry {

    private final Map<String, TypeConverterAdapter> content = new HashMap<>();
    private boolean isSetup = false;

    public void register(TypeConverterAdapter adapter) {
        content.putIfAbsent(adapter.supportedFileType(), adapter);
    }

    public TypeConverterAdapter findByFileType(String fileType) {
        return Optional.ofNullable(content.get(fileType))
                .orElseThrow(() -> new IllegalStateException("Could not find a TypeConverter for type " + fileType));
    }

    public void setupDone() {
        isSetup = true;
    }

    public void clear() {
        isSetup = false;
        content.clear();
    }

    public boolean isSetup() {
        return isSetup;
    }
}
