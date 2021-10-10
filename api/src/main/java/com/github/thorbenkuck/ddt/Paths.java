package com.github.thorbenkuck.ddt;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;

import java.nio.file.Path;

public class Paths {

    public static String nameOnly(Path path) {
        return nameOnly(path.getFileName().toString());
    }

    public static String nameOnly(String fullFileName) {
        int index = fullFileName.indexOf('.');
        if (index > 0) {
            return fullFileName.substring(0, index);
        } else {
            throw new IllegalArgumentException("Could not find a file extension in the filename " + fullFileName);
        }
    }

    public static String testName(Path path, int index, TestScenario annotation) {
//        "[" + index + "] " + Paths.beautifiedName(path)
        String rawResult = annotation.name();

        if (rawResult.isEmpty()) {
            rawResult = TestScenario.DEFAULT_NAME;
        }

        return rawResult.replaceAll("\\{index}", Integer.toString(index))
                .replaceAll("\\{name}", beautifiedName(path))
                .replaceAll("\\{filename}", nameOnly(path))
                .replaceAll("\\{type}", getExtension(path));
    }

    public static String beautifiedName(Path path) {
        String name = nameOnly(path);
        return TestName.produceOf(name);
    }

    public static String getExtension(Path path) {
        return getExtension(path.getFileName().toString());
    }

    public static String getExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index > 0) {
            return filename.substring(index + 1);
        } else {
            throw new IllegalArgumentException("Could not find a file extension in the filename " + filename);
        }
    }
}
