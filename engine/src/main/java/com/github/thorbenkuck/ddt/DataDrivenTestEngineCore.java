package com.github.thorbenkuck.ddt;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class DataDrivenTestEngineCore {

    private static final List<Extension> GLOBAL_EGINE_EXTENSIONS = new ArrayList<>();

    static {
        ServiceLoader.load(Extension.class)
                .forEach(GLOBAL_EGINE_EXTENSIONS::add);
        ServiceLoader.loadInstalled(Extension.class)
                .forEach(GLOBAL_EGINE_EXTENSIONS::add);
    }

    private final List<Extension> engineExtensions = new ArrayList<>(GLOBAL_EGINE_EXTENSIONS);

    public void registerLocally(Extension extension) {
        engineExtensions.add(extension);
    }

    public void beforeEach() {

    }
}
