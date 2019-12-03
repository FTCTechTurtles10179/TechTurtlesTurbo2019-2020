package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class AutoController extends WheelController {
    double cmToClick = 16.5;

    public AutoController(Configurator config) {
        super(config);
    }

    public void moveForwardCentimeters(double distance, double speed) {
        double startingEncoder = super.avgEncoder();
        while (Math.abs((super.avgEncoder() - startingEncoder) - (distance * cmToClick)) <= 0.1 * cmToClick) {
            if (Math.abs((super.avgEncoder() - startingEncoder) - (distance * cmToClick)) >= 5 * cmToClick) {
                super.moveXY(0, speed);
            } else {
                super.moveXY(0, speed/3);
            }
        }
    }

    public void strafeRightCentimeters(double distance, double speed) {
        double startingEncoder = super.avgEncoder();
        while (Math.abs((super.avgEncoder() - startingEncoder) - (distance * cmToClick)) <= 0.1 * cmToClick) {
            if (Math.abs((super.avgEncoder() - startingEncoder) - (distance * cmToClick)) >= 5 * cmToClick) {
                super.moveXY(speed, 0);
            } else {
                super.moveXY(speed/3, 0);
            }
        }
    }
}
