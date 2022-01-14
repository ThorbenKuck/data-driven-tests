package com.github.thorbenkuck.ddt.api.annotations;

import com.github.thorbenkuck.ddt.api.SuiteType;
import com.github.thorbenkuck.ddt.api.annotations.importer.AssertedBy;
import com.github.thorbenkuck.ddt.api.annotations.importer.TestCaseFactories;
import com.github.thorbenkuck.ddt.api.domain.Asserter;
import com.github.thorbenkuck.ddt.api.domain.TestCaseFactory;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class TestScenarioValues {

    private final String suite;
    private final SuiteType suiteType;
    private final String expectedFileFlag;
    private final String name;
    private final List<Class<? extends Asserter>> asserters;
    private final List<Class<? extends TestCaseFactory<?, ?>>> factories;

    public TestScenarioValues(
            String suite,
            SuiteType suiteType,
            String expectedFileFlag,
            String name,
            List<Class<? extends Asserter>> asserters,
            List<Class<? extends TestCaseFactory<?, ?>>> factories
    ) {
        this.suite = suite;
        this.suiteType = suiteType;
        this.expectedFileFlag = expectedFileFlag;
        this.name = name;
        this.asserters = asserters;
        this.factories = factories;
    }

    private static String findName(Method method) {
        return AnnotationCollector.findFirst(TestName.class, method)
                .map(TestName::value)
                .orElseGet(() -> AnnotationCollector.findFirst(TestScenario.class, method)
                        .map(TestScenario::name)
                        .orElseGet(() -> AnnotationCollector.findFirst(TestName.class, method.getDeclaringClass())
                                .map(TestName::value)
                                .orElse(TestScenario.DEFAULT_NAME)));
    }

    private static String findSuiteName(Method method) {
        return AnnotationCollector.findFirst(TestScenario.class, method)
                .map(TestScenario::suite)
                .orElse(TestScenario.DEFAULT_SUITE_NAME);
    }

    private static String findExpectedFileName(Method method) {
        return AnnotationCollector.findFirst(TestScenario.class, method)
                .map(TestScenario::expectedFileFlag)
                .orElse(TestScenario.DEFAULT_EXPECTED_FILE_FLAG);
    }

    private static SuiteType findSuiteType(Method method) {
        return AnnotationCollector.findFirst(TestScenario.class, method)
                .map(TestScenario::suiteType)
                .orElse(TestScenario.DEFAULT_SUITE_TYPE);
    }

    private static List<Class<? extends Asserter>> findAsserters(Method method) {
        return AnnotationCollector.findFirst(TestScenario.class, method)
                .map(TestScenario::asserters)
                .map(Arrays::asList)
                .orElse(AnnotationCollector.of(AssertedBy.class)
                        .searchIn(method)
                        .searchIn(method.getDeclaringClass())
                        .analyze()
                        .flatMap(AssertedBy::value)
                        .asList());
    }

    private static List<Class<? extends TestCaseFactory<?, ?>>> findFactories(Method method) {
        return AnnotationCollector.findFirst(TestScenario.class, method)
                .map(TestScenario::factories)
                .map(Arrays::asList)
                .orElse(AnnotationCollector.of(TestCaseFactories.class)
                        .searchIn(method)
                        .searchIn(method.getDeclaringClass())
                        .analyze()
                        .flatMap(TestCaseFactories::value)
                        .asList());
    }

    public static TestScenarioValues determine(Method method) {
        String name = findName(method);
        String suiteName = findSuiteName(method);
        SuiteType suiteType = findSuiteType(method);
        String expectedFileName = findExpectedFileName(method);
        List<Class<? extends Asserter>> asserters = findAsserters(method);
        List<Class<? extends TestCaseFactory<?, ?>>> factories = findFactories(method);

        return new TestScenarioValues(suiteName, suiteType, expectedFileName, name, asserters, factories);
    }

    public String getSuite() {
        return suite;
    }

    public SuiteType getSuiteType() {
        return suiteType;
    }

    public String getExpectedFileFlag() {
        return expectedFileFlag;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Asserter>[] asserters() {
        return asserters.toArray(new Class[0]);
    }

    public List<Class<? extends TestCaseFactory<?, ?>>> getFactories() {
        return factories;
    }
}
