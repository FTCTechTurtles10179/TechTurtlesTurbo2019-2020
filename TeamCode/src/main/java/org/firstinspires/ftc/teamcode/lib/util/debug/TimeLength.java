package org.firstinspires.ftc.teamcode.lib.util.debug;

public class TimeLength {
    long timeA, timeB;
    public String label;
    
    TimeLength(long timeA, long timeB) {
        this.timeA =  timeA;
        this.timeB = timeB;
    }

    TimeLength(long timeA, long timeB, String label) {
        this.timeA =  timeA;
        this.timeB = timeB;
    }
    
    long getStart() {
        return timeB > timeA ? timeA : timeB;
    }
    
    long getEnd() {
        return timeB > timeA ? timeB : timeA;
    }
    
    long getLength() {
        return timeB > timeA ? timeB - timeA : timeA - timeB;
    }
}
