package com.github.thorbenkuck.ddt.api.identification;

import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MethodSignature {

    private final List<Predicate<Method>> predicates = new ArrayList<>();

    private MethodSignature() {
    }

    public static MethodSignature that() {
        return new MethodSignature();
    }

    public MethodSignature isStatic() {
        predicates.add(method -> Modifier.isStatic(method.getModifiers()));
        return this;
    }

    public MethodSignature isNotStatic() {
        predicates.add(method -> !Modifier.isStatic(method.getModifiers()));
        return this;
    }

    public MethodSignature isAbstract() {
        predicates.add(method -> Modifier.isAbstract(method.getModifiers()));
        return this;
    }

    public MethodSignature isNotAbstract() {
        predicates.add(method -> !Modifier.isAbstract(method.getModifiers()));
        return this;
    }

    public MethodSignature isPublic() {
        predicates.add(method -> Modifier.isPublic(method.getModifiers()));
        return this;
    }

    public MethodSignature isNotPrivate() {
        predicates.add(method -> !Modifier.isPrivate(method.getModifiers()));
        return this;
    }

    public MethodSignature hasReturnValue(Class<?> type) {
        predicates.add(method -> method.getReturnType().equals(type));
        return this;
    }

    public MethodSignature hasAnnotation(Class<? extends Annotation> annotationType) {
        predicates.add(method -> AnnotationCollector.anyMatch(annotationType, method));
        return this;
    }

    public MethodSignature hasAnyAnnotation(Class<? extends Annotation>... annotationType) {
        predicates.add(method -> Arrays.stream(annotationType).anyMatch(type -> AnnotationCollector.anyMatch(type, method)));
        return this;
    }

    public MethodSignature hasParameterCount(int count) {
        predicates.add(method -> method.getParameterCount() == count);
        return this;
    }

    public MethodSignature hasParameterTypes(Class<?>... types) {
        predicates.add(method -> Arrays.equals(method.getParameterTypes(), types));
        return this;
    }

    public MethodSignature reset() {
        predicates.clear();
        return this;
    }

    public boolean matches(Method method) {
        for (Predicate<Method> predicate : predicates) {
            if (!predicate.test(method)) {
                return false;
            }
        }

        return true;
    }

    public Predicate<Method> asPredicate() {
        return this::matches;
    }
}
