package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.BooleanCommand;

import static java.lang.System.currentTimeMillis;

public class Timeout {
    /**
     * rung stopIf.execute() to pause until millis has ellapsed, or stop if
     * @param millis
     * @param stopIf
     */
    public static void waitUnlessInterrupt(long millis, BooleanCommand stopIf) {
        long startingTime = currentTimeMillis();
        boolean stopIf_cached = false;
        while (currentTimeMillis() <= millis + startingTime && !stopIf_cached) {
            stopIf_cached = stopIf.execute();
        }
    }
}
