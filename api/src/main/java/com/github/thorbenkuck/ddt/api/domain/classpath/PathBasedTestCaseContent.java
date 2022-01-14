package com.github.thorbenkuck.ddt.api.domain.classpath;

import com.github.thorbenkuck.ddt.api.annotations.TestScenario;
import com.github.thorbenkuck.ddt.api.domain.AbstractTestCaseContent;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.FileSource;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Optional;

public class PathBasedTestCaseContent extends AbstractTestCaseContent<TestCaseInputFile, TestCaseOutputFile> {

    public PathBasedTestCaseContent(Path input, TestScenario annotation) {
        super(new TestCaseInputFile(input), annotation);
    }

    @Override
    public void validateMatchesMethod(Method method) {
        if (!outputFileExists()) {
            if (method.getParameterCount() == 2 || !method.getReturnType().equals(Void.TYPE)) {
                throw new IllegalStateException("There was an output expected, yet no output data could be found");
            }
        }
    }

    @Override
    protected Optional<TestCaseOutputFile> resolveExpectedOutput(TestCaseInputFile testCaseInput, TestScenario annotation) {
        Path parent = testCaseInput.getPath().getParent();
        return TestCaseOutputFile.resolve(parent, testCaseInput.getName(), annotation.expectedFileFlag());
    }

    @Override
    public TestSource testSource() {
        return FileSource.from(input().getPath().toFile());
    }
}
