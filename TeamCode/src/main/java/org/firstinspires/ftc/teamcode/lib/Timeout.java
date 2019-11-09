package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.BooleanCommand;

import static android.os.SystemClock.sleep;
import static android.os.SystemClock.uptimeMillis;

public class Timeout {
    public static void waitUnlessInterrupt(long millis, BooleanCommand interrupt) {
        long startingTime = uptimeMillis();
        while (uptimeMillis() >= millis + startingTime || interrupt.execute()) {
            sleep(1);
        }
    }
}
