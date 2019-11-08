package org.firstinspires.ftc.teamcode.lib;

public class Smoother {
    public static double smooth(double rawInput, double previousInput){
        double input = (rawInput+previousInput)/2;
        if (Math.abs(input) <= 0.99) {
            return 0.25 * input;
        } else {
            return input;
        }
    }
}
