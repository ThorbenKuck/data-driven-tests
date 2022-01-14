package com.github.thorbenkuck.ddt.api.domain.classpath;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.util.Paths;
import com.github.thorbenkuck.ddt.api.registry.TypeConverterAdapterRegistry;
import com.github.thorbenkuck.ddt.api.domain.TypeConverterAdapter;
import com.github.thorbenkuck.ddt.api.domain.TestCaseInput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestCaseInputFile implements TestCaseInput {

    private final Path path;
    private final String fullFileName;
    private final String fileType;
    private final String name;

    public TestCaseInputFile(Path path) {
        this.path = path;
        this.fullFileName = path.getFileName().toString();
        this.fileType = Paths.getExtension(fullFileName);
        this.name = Paths.nameOnly(fullFileName);
    }

    public Path getPath() {
        return path;
    }

    public String getFileType() {
        return fileType;
    }

    public String getName() {
        return name;
    }

    @Override
    public String type() {
        return fileType;
    }

    @Override
    public String rawName() {
        return fullFileName;
    }

    @Override
    public String fullPath() {
        return this.path.toString();
    }

    public byte[] readContent() {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read file " + fullFileName, e);
        }
    }

    @Override
    public <T> T convertTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry) {
        TypeConverterAdapter converter = adapterRegistry.findByFileType(type());
        return converter.convert(readContent(), type);
    }

    @Override
    public String testName(int index, TestScenario annotation) {
        return Paths.testName(path, index, annotation);
    }
}
