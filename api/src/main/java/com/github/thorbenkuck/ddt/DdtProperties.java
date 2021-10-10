package com.github.thorbenkuck.ddt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class DdtProperties {

    private static final Properties properties = new Properties();

    static {
        InputStream resourceAsStream = DdtProperties.class.getClassLoader().getResourceAsStream("ddt.properties");

        if (resourceAsStream != null) {
            try {
                properties.load(resourceAsStream);
            } catch (IOException e) {
                throw new PropertySyntaxError("ddt.properties", e);
            }
        }

        // The DefaultProperties class provides all properties with default values
        // in its constructor. I know, IntelliJ cannot understand this, therefore:
        // noinspection RedundantOperationOnEmptyContainer
        new DefaultProperties().forEach(properties::putIfAbsent);
        // TODO Add support for System.properties
    }

    private static final String FALLBACK_ASSERTER_KEY = "ddt.asserters.fallback";
    private static final String SKIP_FAILED_ASSERTER_INSTANTIATION_KEY = "ddt.asserters.skip-failed-instantiations";
    private static final String SUITE_ROOT_FOLDER = "ddt.suits.root-folder";
    private static final String CLASS_PATH_SCANNER_FOR_FACTORIES_KEY = "ddt.content-factory.class-path-scanning.enabled";
    private static final String CLASS_PATH_SCANNER_FOR_FACTORIES_SUB_PACKAGE = "ddt.content-factory.class-path-scanning.base-package";

    public static Optional<String> fallbackAsserter() {
        return Optional.ofNullable(properties.getProperty(FALLBACK_ASSERTER_KEY));
    }

    public static boolean contentFactoryClassPathScanningEnabled() {
        return Boolean.parseBoolean(properties.getProperty(CLASS_PATH_SCANNER_FOR_FACTORIES_KEY));
    }

    public static boolean skipFailedAsserterInstantiation() {
        return Boolean.parseBoolean(properties.getProperty(SKIP_FAILED_ASSERTER_INSTANTIATION_KEY));
    }

    public static OptionalConsumer<String> contentFactoryClassPathBasePackage() {
        return OptionalConsumer.of(properties.getProperty(CLASS_PATH_SCANNER_FOR_FACTORIES_SUB_PACKAGE));
    }

    public static String suitsRoot() {
        return properties.getProperty(SUITE_ROOT_FOLDER);
    }

    private static class DefaultProperties extends Properties {
        public DefaultProperties() {
            put(CLASS_PATH_SCANNER_FOR_FACTORIES_KEY, "true");
            put(SKIP_FAILED_ASSERTER_INSTANTIATION_KEY, "true");
            put(SUITE_ROOT_FOLDER, "suits");
        }
    }
}
