package com.github.thorbenkuck.ddt.api.annotations.importer.meta;

import com.github.thorbenkuck.ddt.api.annotations.TestScenarioValues;
import com.github.thorbenkuck.ddt.properties.DdtProperties;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import com.github.thorbenkuck.ddt.collection.OrderedSet;
import com.github.thorbenkuck.ddt.api.annotations.importer.AssertedBy;
import com.github.thorbenkuck.ddt.api.domain.Asserter;

import java.lang.reflect.Method;
import java.util.Arrays;

public class AsserterMetaData {

    private final OrderedSet<Class<? extends Asserter>> asserters;

    public AsserterMetaData(OrderedSet<Class<? extends Asserter>> annotations) {
        this.asserters = annotations;
    }

    public static AsserterMetaData of(Method method, TestScenarioValues testScenario) {
        OrderedSet<Class<? extends Asserter>> classes = AnnotationCollector.of(AssertedBy.class)
                .searchIn(method)
                .analyze()
                .map(AssertedBy::value)
                .prepend(testScenario.asserters())
                .flatMap(it -> it)
                .asOrderedSet();

        if (classes.isEmpty()) {
            fallbackAsserter().sinkInto(classes);
        }
        return new AsserterMetaData(classes);
    }

    private static OrderedSet<Class<? extends Asserter>> fallbackAsserter() {
        return DdtProperties.fallbackAsserter()
                .map(property -> Arrays.stream(property.split(","))
                        .<Class<? extends Asserter>>map(AsserterMetaData::checkedClassFromName)
                        .collect(OrderedSet.collector())
                ).orElseGet(OrderedSet::empty);
    }

    private static Class<? extends Asserter> checkedClassFromName(String name) {
        try {
            Class<?> declaredDefault = Class.forName(name);
            if (!Asserter.class.isAssignableFrom(declaredDefault)) {
                throw new IllegalArgumentException("The class " + declaredDefault + " is not implementing the Asserter interface");
            }
            return (Class<? extends Asserter>) declaredDefault;
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not find the default asserter class " + name, e);
        }
    }

    public OrderedSet<Class<? extends Asserter>> getAsserters() {
        return asserters;
    }
}
