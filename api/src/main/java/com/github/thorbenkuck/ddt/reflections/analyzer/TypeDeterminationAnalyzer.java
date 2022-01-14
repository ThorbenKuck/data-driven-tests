package com.github.thorbenkuck.ddt.reflections.analyzer;

import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TypeDeterminationAnalyzer implements Analyzer<AnnotatedElement> {

    private static final Map<Class<?>, Analyzer<?>> analyzerMap = new HashMap<>();

    static {
        register(Method.class, Analyzer.method);
        register(Annotation.class, Analyzer.annotation);
    }

    public static <T> void register(Class<T> type, Analyzer<T> analyzer) {
        analyzerMap.put(type, analyzer);
    }

    @Override
    public <T extends Annotation> OrderedSet<T> findIn(Class<T> annotationType, AnnotatedElement annotatedElement) {
        return invokeFor(annotationType, annotatedElement);
    }

    private <T, S extends Annotation> OrderedSet<S> invokeFor(Class<S> annotationType, T t) {
        Analyzer<T> analyzer;
        if(t instanceof Annotation) {
            analyzer = (Analyzer<T>) analyzerMap.get(Annotation.class);
        } else if(t instanceof Class) {
            analyzer = (Analyzer<T>) Analyzer.clazz;
        }else {
            analyzer = (Analyzer<T>) analyzerMap.get(t.getClass());
        }

        if(analyzer == null) {
            throw new IllegalArgumentException("[TECHNICAL]: Unsupported annotated element type " + t.getClass());
        }
        return analyzer.findIn(annotationType, t);
    }
}
