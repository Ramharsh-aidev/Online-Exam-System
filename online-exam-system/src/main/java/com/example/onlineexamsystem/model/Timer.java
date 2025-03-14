package com.example.onlineexamsystem.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Timer class to manage exam duration and timeout.
 */
public class Timer {
    private final Duration duration;
    private LocalDateTime startTime;
    private ScheduledExecutorService scheduler;
    private final Runnable timeoutCallback;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * Constructor for Timer.
     *
     * @param duration        Duration of the timer.
     * @param timeoutCallback Runnable to execute when the timer expires.
     */
    public Timer(Duration duration, Runnable timeoutCallback) {
        this.duration = duration;
        this.timeoutCallback = timeoutCallback;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts the timer.
     */
    public void start() {
        if (isRunning.compareAndSet(false, true)) {
            this.startTime = LocalDateTime.now();
            scheduler.schedule(timeoutCallback, duration.toMinutes(), TimeUnit.MINUTES);
            System.out.println("Timer started for " + duration.toMinutes() + " minutes.");
        }
    }

    /**
     * Stops the timer.
     */
    public void stop() {
        if (isRunning.compareAndSet(true, false)) {
            scheduler.shutdownNow();
            System.out.println("Timer stopped.");
        }
    }

    /**
     * Gets the time remaining in the timer.
     *
     * @return Duration remaining.
     */
    public Duration getTimeRemaining() {
        if (!isRunning.get()) {
            return Duration.ZERO;
        }
        Duration elapsed = Duration.between(startTime, LocalDateTime.now());
        Duration remaining = duration.minus(elapsed);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    /**
     * Gets the formatted time remaining string (MM:SS).
     *
     * @return Formatted time remaining string.
     */
    public String getTimeRemainingFormatted() {
        long minutes = getTimeRemaining().toMinutes();
        long seconds = getTimeRemaining().minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d", minutes, seconds);
    }
}