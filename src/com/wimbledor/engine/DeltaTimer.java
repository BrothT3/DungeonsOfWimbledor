package com.wimbledor.engine;

/**
 * Tracks elapsed real time between update() calls,
 * and lets you query when a given interval has passed.
 */
public class DeltaTimer {
    private long lastNanos;
    private double accumSeconds;

    public DeltaTimer() {
        this.lastNanos    = System.nanoTime();
        this.accumSeconds = 0.0;
    }

    /**
     * Call once per loop to update the accumulated time.
     */
    public void update() {
        long now    = System.nanoTime();
        double dt   = (now - lastNanos) / 1_000_000_000.0;
        lastNanos   = now;
        accumSeconds += dt;
    }

    /**
     * Returns true if at least `seconds` have elapsed
     * since the last time this returned true (or since construction).
     * Automatically subtracts off the interval so you can
     * continue timing for the next tick.
     */
    public boolean reached(double seconds) {
        if (accumSeconds >= seconds) {
            accumSeconds -= seconds;
            return true;
        }
        return false;
    }

    /** Reset the timer back to zero. */
    public void reset() {
        lastNanos    = System.nanoTime();
        accumSeconds = 0.0;
    }
}