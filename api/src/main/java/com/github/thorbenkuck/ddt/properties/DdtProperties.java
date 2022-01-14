package com.github.thorbenkuck.ddt.properties;

import com.github.thorbenkuck.ddt.util.OptionalConsumer;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DdtProperties {

    private static final Properties properties = new Properties();

    public static final String FALLBACK_ASSERTER_KEY = "ddt.asserters.fallback";
    public static final String ASSERTION_FAILED_FOR_ASSERTER_ERROR_MESSAGE_KEY = "ddt.asserters.error-message";
    public static final String SKIP_FAILED_ASSERTER_INSTANTIATION_KEY = "ddt.asserters.skip-failed-instantiations";

    public static final String SUITE_ROOT_FOLDER = "ddt.suits.root-folder";

    public static final String CLASS_PATH_SCANNER_FOR_FACTORIES_KEY = "ddt.content-factories.scanning.class-path-scanning-enabled";
    public static final String CLASS_PATH_SCANNER_FOR_FACTORIES_SUB_PACKAGE = "ddt.content-factories.scanning.base-package";
    public static final String ALL_FACTORIES_ARE_LAZY = "ddt.content-factories.instantiation.lazy";

    public static final String ACTIVE_ENGINE_CORE_KEY = "ddt.engine-core.active";
    public static final String ENGINE_CORE_SINGLETON_KEY = "ddt.engine-core.singleton";

    public static final String INTROSPECT_ON_DECLARED_CLASS = "ddt.introspection.method.introspect-on-declared-class";
    public static final String INTROSPECTION_ON_INHERITED_CLASS = "ddt.introspection.method.introspect-on-inherited-class";

    public static final String TEST_INSTANCE_DEFAULT = "ddt.tests.default-instance-type";
    public static final String ASSERTION_FAILED_ERROR_MESSAGE_KEY = "ddt.tests.error-message";

    public static final String FAIL_ON_MISSING_PRECONDITION_KEY = "ddt.execution.fail-on-missing-precondition";

    public static final List<String> allKeys = Arrays.asList(
            FALLBACK_ASSERTER_KEY,
            SKIP_FAILED_ASSERTER_INSTANTIATION_KEY,
            SUITE_ROOT_FOLDER,
            CLASS_PATH_SCANNER_FOR_FACTORIES_KEY,
            CLASS_PATH_SCANNER_FOR_FACTORIES_SUB_PACKAGE,
            ACTIVE_ENGINE_CORE_KEY,
            ENGINE_CORE_SINGLETON_KEY,
            INTROSPECT_ON_DECLARED_CLASS,
            INTROSPECTION_ON_INHERITED_CLASS,
            ALL_FACTORIES_ARE_LAZY,
            TEST_INSTANCE_DEFAULT,
            ASSERTION_FAILED_FOR_ASSERTER_ERROR_MESSAGE_KEY,
            ASSERTION_FAILED_ERROR_MESSAGE_KEY,
            FAIL_ON_MISSING_PRECONDITION_KEY
    );

    static {
        // Stage 1: Take from Environment Variables
        takeRelevantKeys(propertiesFromEnvironment());

        // Stage 2: Take from System.getProperties()
        takeRelevantKeys(System.getProperties());

        // Stage 3: Take from ddt.properties file in classpath and put in System.getProperties is absent
        Properties loadedProperties = propertiesFromFile();
        takeRelevantKeys(loadedProperties);
        copyRelevantKeys(loadedProperties, System.getProperties());

        // Stage 4: Take default properties
        takeRelevantKeys(new DefaultProperties());
    }

    public static Properties propertiesFromEnvironment() {
        Properties environmentProperties = new Properties();
        environmentProperties.putAll(System.getenv());
        return environmentProperties;
    }

    public static Properties propertiesFromFile() {
        String propertyLocation = System.getProperty("ddt.properties.location", "ddt.properties");
        Properties loadedProperties = new Properties();
        InputStream resourceAsStream = DdtProperties.class.getClassLoader().getResourceAsStream(propertyLocation);
        if (resourceAsStream != null) {
            try {
                loadedProperties.load(resourceAsStream);
            } catch (IOException e) {
                throw new PropertySyntaxError(propertyLocation, e);
            }
        }

        return loadedProperties;
    }

    public static void takeRelevantKeys(Properties origin) {
        copyRelevantKeys(origin, properties);
    }

    public static void copyRelevantKeys(Properties origin, Properties target) {
        allKeys.forEach(key -> {
            String property = origin.getProperty(key);
            if(property != null) {
                target.putIfAbsent(key, property);
            }
        });
    }

    public static void takeAndOverrideRelevantKeys(Properties origin) {
        copyAndOverrideRelevantKeys(origin, properties);
    }

    public static void copyAndOverrideRelevantKeys(Properties origin, Properties target) {
        allKeys.forEach(key -> {
            String property = origin.getProperty(key);
            if(property != null) {
                target.setProperty(key, property);
            }
        });
    }

    public static void overrideProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public static void trySetProperty(String key, String value) {
        properties.putIfAbsent(key, value);
    }

    public static Properties access() {
        return new Properties(properties);
    }

    // #### Methods to get all the values ####

    public static Optional<String> fallbackAsserter() {
        return Optional.ofNullable(properties.getProperty(FALLBACK_ASSERTER_KEY));
    }

    public static boolean contentFactoryClassPathScanningEnabled() {
        return Boolean.parseBoolean(properties.getProperty(CLASS_PATH_SCANNER_FOR_FACTORIES_KEY));
    }

    public static boolean skipFailedAsserterInstantiation() {
        return Boolean.parseBoolean(properties.getProperty(SKIP_FAILED_ASSERTER_INSTANTIATION_KEY));
    }

    public static boolean introspectOnDeclaredClass() {
        return Boolean.parseBoolean(properties.getProperty(INTROSPECT_ON_DECLARED_CLASS));
    }

    public static boolean introspectionOnInheritedClass() {
        return Boolean.parseBoolean(properties.getProperty(INTROSPECTION_ON_INHERITED_CLASS));
    }

    public static boolean allFactoriesAreInstantiatedLazy() {
        return Boolean.parseBoolean(properties.getProperty(ALL_FACTORIES_ARE_LAZY));
    }

    public static boolean isEngineCoreSingleton() {
        return Boolean.parseBoolean(properties.getProperty(ENGINE_CORE_SINGLETON_KEY));
    }

    public static String caseErrorMessage() {
        return properties.getProperty(ASSERTION_FAILED_FOR_ASSERTER_ERROR_MESSAGE_KEY);
    }

    public static String testErrorMessage() {
        return properties.getProperty(ASSERTION_FAILED_ERROR_MESSAGE_KEY);
    }

    public static TestInstance.Lifecycle defaultTestInstanceType() {
        return TestInstance.Lifecycle.valueOf(properties.getProperty(TEST_INSTANCE_DEFAULT));
    }

    public static OptionalConsumer<String> contentFactoryClassPathBasePackage() {
        return OptionalConsumer.of(properties.getProperty(CLASS_PATH_SCANNER_FOR_FACTORIES_SUB_PACKAGE));
    }

    public static String suitsRoot() {
        return properties.getProperty(SUITE_ROOT_FOLDER);
    }

    public static Optional<String> activeTestScenarioContext() {
        return Optional.ofNullable(properties.getProperty(ACTIVE_ENGINE_CORE_KEY));
    }

    public static Boolean failOnMissingPrecondition() {
        return Boolean.parseBoolean(properties.getProperty(FAIL_ON_MISSING_PRECONDITION_KEY));
    }

    private static class DefaultProperties extends Properties {
        private final Map<String, String> environmentVariables = System.getenv();

        public DefaultProperties() {
            setPropertyOrDefault(CLASS_PATH_SCANNER_FOR_FACTORIES_KEY, "true");
            setPropertyOrDefault(SKIP_FAILED_ASSERTER_INSTANTIATION_KEY, "true");
            setPropertyOrDefault(SUITE_ROOT_FOLDER, "suits");
            setPropertyOrDefault(INTROSPECT_ON_DECLARED_CLASS, "true");
            setPropertyOrDefault(INTROSPECTION_ON_INHERITED_CLASS, "false");
            setPropertyOrDefault(ALL_FACTORIES_ARE_LAZY, "false");
            setPropertyOrDefault(ENGINE_CORE_SINGLETON_KEY, "true");
            setPropertyOrDefault(FAIL_ON_MISSING_PRECONDITION_KEY, "false");
            setPropertyOrDefault(TEST_INSTANCE_DEFAULT, TestInstance.Lifecycle.PER_METHOD.toString());
            setPropertyOrDefault(ASSERTION_FAILED_FOR_ASSERTER_ERROR_MESSAGE_KEY, "{{method}}({{case}}) [{{asserter}}]: {{message}} {{ls}}");
            setPropertyOrDefault(ASSERTION_FAILED_ERROR_MESSAGE_KEY, "{{method}}({{case}}): {{message}} {{ls}}");
        }

        private void setPropertyOrDefault(String key, String providedValue) {
            String defaultKeyName = "default." + key;
            if(environmentVariables.containsKey(defaultKeyName)) {
                setProperty(key, environmentVariables.get(defaultKeyName));
            } else {
                String propertyValue = System.getProperty(defaultKeyName, providedValue);
                setProperty(key, propertyValue);
            }
        }
    }
}
