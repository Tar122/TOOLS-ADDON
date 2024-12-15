package zeb.bws56.pvp.util;

public class TimerUtil {
    private long lastMS = 0;

    // Checks if the specified delay has passed
    public boolean hasPassed(long delay) {
        return System.currentTimeMillis() - lastMS >= delay;
    }

    // Resets the timer to the current system time
    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    // Gets the current time passed since last reset
    public long getTime() {
        return System.currentTimeMillis() - lastMS;
    }
}
