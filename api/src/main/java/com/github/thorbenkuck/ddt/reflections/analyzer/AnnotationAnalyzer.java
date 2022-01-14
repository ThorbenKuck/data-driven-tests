package com.github.thorbenkuck.ddt.reflections.analyzer;

import com.github.thorbenkuck.ddt.collection.OrderedSet;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class AnnotationAnalyzer implements Analyzer<Annotation> {

    @Override
    public <T extends Annotation> OrderedSet<T> findIn(Class<T> annotationType, Annotation annotation) {
        final OrderedSet<T> result = OrderedSet.create();
        final List<Annotation> analyzed = new ArrayList<>();
        findInAndMutate(annotationType, annotation.annotationType().getDeclaredAnnotations(), analyzed, result);
        analyzed.clear();

        return result;
    }

    private <T extends Annotation> void findInAndMutate(
            Class<T> annotationType,
            Annotation[] annotations,
            List<Annotation> analyzed,
            OrderedSet<T> result
    ) {
        for (Annotation declaredAnnotation : annotations) {
            if(analyzed.contains(declaredAnnotation)) {
                continue;
            }

            analyzed.add(declaredAnnotation);
            if (declaredAnnotation.annotationType().equals(annotationType)) {
                result.add((T) declaredAnnotation);
            } else {
                findInAndMutate(annotationType, declaredAnnotation.annotationType().getDeclaredAnnotations(), analyzed, result);
            }
        }
    }
}
