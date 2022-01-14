package com.github.thorbenkuck.ddt.api.identification;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public class IsTestMethodPredicate implements Predicate<Method> {

    private static final MethodSignature matchingMethodSignature = MethodSignature.that()
            .hasAnnotation(TestScenario.class)
            .isNotAbstract();
    private static final IsTestMethodPredicate instance = new IsTestMethodPredicate();

    public static IsTestMethodPredicate get() {
        return instance;
    }

    @Override
    public boolean test(Method method) {
        return matchingMethodSignature.matches(method);
    }
}
