package com.github.thorbenkuck.ddt.api.domain;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class StopWatch {

    private Instant start = null;

    public static StopWatch open() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        return stopWatch;
    }

    public void clear() {
        start = null;
    }

    public void start() {
        start = Instant.now();
    }

    public Duration stop() {
        if (start == null) {
            throw new IllegalStateException("StopWatch has not been started!");
        }

        Instant stop = Instant.now();
        Duration result = Duration.between(start, stop);
        clear();
        return result;
    }

    public long stop(TimeUnit timeUnit) {
        return stop().get(timeUnit.toChronoUnit());
    }
}
