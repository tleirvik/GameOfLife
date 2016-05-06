package model.util;

/**
 *  This class is a simple Java performance timer used for tracking time elapsed for a method to run
 *  @version 1.0 Initial
 *  @deprecated Obsolete
 */
@Deprecated
public class Stopwatch {
    long startTime;
    long endTime;
    long duration;
    String message;

    /**
     *  Constructor that creates a Stopwatch object with the name of the method beeing timed
     *
     * @param methodName The name of the method beeing timed
     */
    public Stopwatch(String methodName) {
        this.message = methodName;
    }

    /**
     *  This method starts the stopwatch
     */
    public void start() {
        startTime = System.nanoTime();
    }

    /**
     *  This method stops the stopwatch and prints out the elapsed time to the console
     */
    public void stop() {
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("Method: " + message + " Time elapsed: " + duration);
    }

}
