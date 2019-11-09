package org.firstinspires.ftc.teamcode.lib;

public class Smoother {
    public static double smooth(double rawInput){
        double input = rawInput;
        if (Math.abs(input) <= 0.99) {
            return 0.25 * input;
        } else {
            return input;
        }
    }
}
