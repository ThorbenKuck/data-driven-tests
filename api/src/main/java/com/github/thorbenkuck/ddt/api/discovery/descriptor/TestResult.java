package com.github.thorbenkuck.ddt.api.discovery.descriptor;

import com.github.thorbenkuck.ddt.api.annotations.marker.TestFailException;
import com.github.thorbenkuck.ddt.reflections.AnnotationCollector;
import org.junit.platform.commons.util.ToStringBuilder;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TestResult {

    public TestResult compact() {
        if (status == Status.SUCCESSFUL) {
            return this;
        }

        StringBuilder messageBuilder = new StringBuilder("Tests under this section failed");

        if (throwable != null) {
            messageBuilder.append(System.lineSeparator()).append(throwable.getMessage());
            for (Throwable suppressed : throwable.getSuppressed()) {
                String message = Arrays.stream(suppressed.getMessage().split(System.lineSeparator())).filter(it -> !it.isEmpty())
                        .collect(Collectors.joining(System.lineSeparator()));

                messageBuilder.append(System.lineSeparator()).append(message);
            }
        }

        return TestResult.failed(new AssertionError(messageBuilder.toString()));
    }

    public enum Status {
        SUCCESSFUL {
            @Override
            boolean okay() {
                return true;
            }
        },
        ABORTED {
            boolean skipped() {
                return true;
            }
        },
        FAILED;

        boolean okay() {
            return false;
        }

        boolean skipped() {
            return false;
        }
    }

    private static final TestResult SUCCESSFUL_RESULT = new TestResult(Status.SUCCESSFUL, null);

    public static TestResult successful() {
        return SUCCESSFUL_RESULT;
    }

    public static TestResult aborted(Throwable throwable) {
        return new TestResult(Status.ABORTED, throwable);
    }

    public static TestResult failed(Throwable throwable) {
        return new TestResult(Status.FAILED, throwable);
    }

    public static TestResult of(Throwable throwable) {
        if (isFailedException(throwable)) {
            return failed(throwable);
        } else {
            return aborted(throwable);
        }
    }

    public static boolean isFailedException(Throwable throwable) {
        if (throwable instanceof AssertionError) {
            return true;
        }

        return AnnotationCollector.anyMatch(TestFailException.class, throwable.getClass());
    }

    private final Status status;
    private final Throwable throwable;

    private TestResult(Status status, Throwable throwable) {
        this.status = Objects.requireNonNull(status, "Status must not be null");
        this.throwable = throwable;
    }

    public boolean isSuccessful() {
        return status.okay();
    }

    public boolean wasSkipped() {
        return status.skipped();
    }

    public TestResult.Status getStatus() {
        return status;
    }

    public Optional<Throwable> getThrowable() {
        return Optional.ofNullable(throwable);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("status", status)
                .append("throwable", throwable)
                .toString();
    }

    public TestResult and(TestResult other) {
        if (isSuccessful() && other.isSuccessful()) {
            return other;
        }

        Status nextStatus;
        if (other.status == Status.FAILED || status == Status.FAILED) {
            nextStatus = Status.FAILED;
        } else {
            if (!other.isSuccessful()) {
                nextStatus = other.status;
            } else {
                nextStatus = status;
            }
        }

        Throwable throwable = null;
        if (getThrowable().isPresent()) {
            throwable = getThrowable().get();
            if (other.getThrowable().isPresent()) {
                throwable.addSuppressed(other.throwable);
            }
        } else if (other.getThrowable().isPresent()) {
            throwable = other.getThrowable().get();
        }

        return new TestResult(nextStatus, throwable);
    }
}
