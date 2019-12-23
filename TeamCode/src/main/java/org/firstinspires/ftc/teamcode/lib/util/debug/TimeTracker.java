package org.firstinspires.ftc.teamcode.lib.util.debug;

import java.util.ArrayList;

import static java.lang.System.currentTimeMillis;

public class TimeTracker {
    ArrayList<TimeLength> timeLengths;
    long timeA;
    long timeB;

    public TimeTracker() {
        timeLengths = new ArrayList<>();
        timeA = 0;
        timeB = 0;
    }

    public void start() {
        timeA = currentTimeMillis();
    }

    public TimeLength end(String name) {
        timeB = currentTimeMillis();
        TimeLength timeLength = new TimeLength(timeA, timeB, name);
        timeLengths.add(timeLength);
        timeA = 0;
        timeB = 0;
        return timeLength;
    }

    public TimeLength get(String name) {
        for (TimeLength t : timeLengths) {
            if (t.label == name) {
                return t;
            }
        }
        return null;
    }

    public long avgTime() {
        long totalTime = 0;
        for (TimeLength t : timeLengths) {
            totalTime += t.getLength();
        }
        if (totalTime == 0) return 0;
        return totalTime / timeLengths.size();
    }
}
