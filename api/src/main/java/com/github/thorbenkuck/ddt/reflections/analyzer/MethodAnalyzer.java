package com.github.thorbenkuck.ddt.reflections.analyzer;

import com.github.thorbenkuck.ddt.properties.DdtProperties;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import org.assertj.core.util.introspection.IntrospectionError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class MethodAnalyzer implements Analyzer<Method> {

    private final ClassAnalyzer classAnalyzer = new ClassAnalyzer();
    private final DeclaredAnnotationAnalyzer declaredAnnotationAnalyzer = new DeclaredAnnotationAnalyzer();

    @Override
    public <T extends Annotation> @NotNull OrderedSet<T> findIn(@NotNull Class<T> annotationType, @NotNull Method method) {
        OrderedSet<T> result = OrderedSet.create();

        if (DdtProperties.introspectOnDeclaredClass()) {
            Method originalMethod = tryFindOriginalMethod(method);

            if (originalMethod.isAnnotationPresent(annotationType)) {
                result.add(originalMethod.getAnnotation(annotationType));
            } else if (annotationMayBeMetaAnnotated(annotationType)) {
                declaredAnnotationAnalyzer.findIn(annotationType, originalMethod).sinkInto(result);
            }
        }

        if (DdtProperties.introspectionOnInheritedClass()) {
            if (method.isAnnotationPresent(annotationType)) {
                result.add(method.getAnnotation(annotationType));
            } else if (annotationMayBeMetaAnnotated(annotationType)) {
                declaredAnnotationAnalyzer.findIn(annotationType, method).sinkInto(result);
            }
        }

        if (annotationMayBeOnClass(annotationType)) {
            classAnalyzer.findIn(annotationType, method.getDeclaringClass());
        }
        return result;
    }

    private boolean annotationMayBeOnClass(@NotNull Class<? extends Annotation> annotation) {
        if (!annotation.isAnnotationPresent(Target.class)) {
            return true;
        } else {
            return Arrays.stream(annotation.getAnnotation(Target.class).value())
                    .anyMatch(it -> it == ElementType.TYPE);
        }
    }

    private boolean annotationMayBeMetaAnnotated(@NotNull Class<? extends Annotation> annotation) {
        if (!annotation.isAnnotationPresent(Target.class)) {
            return true;
        } else {
            return Arrays.stream(annotation.getAnnotation(Target.class).value())
                    .anyMatch(it -> it == ElementType.ANNOTATION_TYPE);
        }
    }

    private @NotNull Method tryFindOriginalMethod(@NotNull Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        return Arrays.stream(declaringClass.getMethods())
                .filter(it -> methodEquals(it, method))
                .findFirst()
                .orElse(method);
    }

    private boolean methodEquals(@Nullable Method method1, @Nullable Method method2) {
        if(method1 == null && method2 == null) {
            return true;
        } else if(method1 == null) {
            return false;
        } else if(method2 == null) {
            return false;
        }

        if (method1.getName().equals(method2.getName())) {
            if (!method1.getReturnType().equals(method2.getReturnType())) {
                return false;
            }

            return equalParamTypes(method1.getParameterTypes(), method2.getParameterTypes());
        }
        return false;
    }

    boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
        if (params1.length == params2.length) {
            for (int i = 0; i < params1.length; ++i) {
                if (params1[i] != params2[i]) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
