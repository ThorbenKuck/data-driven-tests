package com.github.thorbenkuck.framework;

import java.util.HashMap;
import java.util.Map;

public class DAO {

    private static final Map<String, Map<String, Object>> tables = new HashMap<>();

    public static <T> Map<String, T> getOrCreateTable(Class<T> type) {
        String name = type.getSimpleName();
        if(!tables.containsKey(name)) {
            tables.put(name, new HashMap<>());
        }

        return (Map<String, T>) tables.get(name);
    }

    public static void reset() {
        tables.forEach((key, value) -> {
            value.clear();
        });
    }
}
