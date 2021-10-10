package com.github.thorbenkuck.ddt.api.domain.classpath;

import com.github.thorbenkuck.ddt.Paths;
import com.github.thorbenkuck.ddt.api.domain.TestCaseOutput;
import com.github.thorbenkuck.ddt.api.domain.TypeConverterAdapter;
import com.github.thorbenkuck.ddt.api.services.adapter.TypeConverterAdapterRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class TestCaseOutputFile implements TestCaseOutput {

    private final Path path;
    private final String fileType;
    private final String name;

    public TestCaseOutputFile(String name, String fileType, Path path) {
        this.name = name;
        this.fileType = fileType;
        this.path = path;
    }

    public static Optional<TestCaseOutputFile> resolve(Path parent, String name, String expectedFlag) {
        try {
            return Files.list(parent)
                    .filter(path -> path.getFileName().toString().matches(name + "[.]" + expectedFlag + "[.].[a-zA-Z0-9]*$"))
                    .findFirst()
                    .map(path -> {
                        String extension = Paths.getExtension(path.getFileName().toString());
                        return new TestCaseOutputFile(name, extension, path);
                    });
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String type() {
        return fileType;
    }

    @Override
    public String rawName() {
        return name;
    }

    @Override
    public <T> T convertTo(Class<T> type, TypeConverterAdapterRegistry adapterRegistry) {
        TypeConverterAdapter converter = adapterRegistry.findByFileType(type());
        return converter.convert(readContent(), type);
    }

    public byte[] readContent() {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
