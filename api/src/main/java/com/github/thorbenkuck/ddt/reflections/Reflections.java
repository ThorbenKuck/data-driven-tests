package com.github.thorbenkuck.ddt.reflections;

import com.github.thorbenkuck.ddt.api.annotations.importer.meta.ConsumesMethodMetaData;
import org.junit.platform.commons.util.ReflectionUtils;

public class Reflections {

    public static <T> T newInstance(Class<T> clazz, Object... args) {
        T t = ReflectionUtils.newInstance(clazz, args);
        invokeInheritedMethods(t);
        return t;
    }

    // This stupid method is required because of
    // the way ? extends T is resolved. If you pass
    // it into here, the generic `? extends ? extends T`
    // Cannot easily be resolved.
    public static <T> T newTypedInstance(Class<T> type, Class<? extends T> clazz, Object... args) {
        T t = ReflectionUtils.newInstance(clazz, args);
        invokeInheritedMethods(t);
        return t;
    }

    // This stupid method is required because of
    // the way ? extends T is resolved. If you pass
    // it into here, the generic `? extends ? extends T`
    // Cannot easily be resolved.
    public static <T> T newTypedInstance(Class<T> type, Class<? extends T> clazz, Class<?> testScenario, Object... args) {
        T t = ReflectionUtils.newInstance(clazz, args);
        invokeInheritedMethods(t, testScenario);
        return t;
    }

    public static <T> void invokeInheritedMethods(T t, Class<?> testScenario) {
        ConsumesMethodMetaData.extractValidOf(t.getClass(), testScenario).forEach(metaData -> metaData.invoke(t));
    }

    public static <T> void invokeInheritedMethods(T t) {
        ConsumesMethodMetaData.extractValidOf(t.getClass()).forEach(metaData -> metaData.invoke(t));
    }
}
