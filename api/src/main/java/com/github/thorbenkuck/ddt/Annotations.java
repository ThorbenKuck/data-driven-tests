package com.github.thorbenkuck.ddt;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Annotations {

    public static <T extends Annotation> T deepFindFirstIn(Method method, Class<T> annotationType) {
        return deepFindIn(method.getAnnotations(), annotationType).get(0);
    }

    public static <T extends Annotation> List<T> deepFindIn(Method method, Class<T> annotationType) {
        if(method.getAnnotation(annotationType) != null) {
            return Collections.singletonList(method.getAnnotation(annotationType));
        }
        return deepFindIn(method.getAnnotations(), annotationType);
    }

    public static <T extends Annotation> List<T> deepFindIn(Class<?> container, Class<T> annotationType) {
        if(container.getAnnotation(annotationType) != null) {
            return Collections.singletonList(container.getAnnotation(annotationType));
        }
        return deepFindIn(container.getDeclaredAnnotations(), annotationType);
    }

    public static <T extends Annotation> List<T> deepFindIn(Annotation[] array, Class<T> annotationType) {
        final List<T> result = new ArrayList<>();
        for (Annotation declaredAnnotation : array) {
            if (declaredAnnotation.annotationType().equals(annotationType)) {
                result.add((T) declaredAnnotation);
            } else {
                result.addAll(deepFindIn(declaredAnnotation, annotationType));
            }
        }
        return result;
    }

    public static <T extends Annotation> List<T> deepFindIn(Annotation annotation, Class<T> annotationType) {
        if (annotation.annotationType().equals(Documented.class)
                || annotation.annotationType().equals(Retention.class)
                || annotation.annotationType().equals(Target.class)) {
            return new ArrayList<>();
        }

        Annotation[] declaredAnnotations = annotation.annotationType().getDeclaredAnnotations();
        final List<T> result = new ArrayList<>();

        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation.annotationType().equals(annotationType)) {
                result.add((T) declaredAnnotation);
            } else {
                result.addAll(deepFindIn(declaredAnnotation, annotationType));
            }
        }
        return result;
    }

}
