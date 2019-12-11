package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.State;

public class AutonomousLibrary extends Configurator {
    double cmToClickForward = 16.5;
    double cmToClickRight = 24.3;

    public void moveForwardCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.avgEncoder();
        stateMachine.addState(new State(() -> {
            if (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) >= 5 * cmToClickForward) {
                wheelController.moveXY(0, speed);
            } else {
                wheelController.moveXY(0, speed/5);
            }
            boolean stop = (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) <= 0.5 * cmToClickForward);
            if (debugMode) telemetry.addData("AutoLibStopWheels", stop);
            return stop;
        }, () -> {
            wheelController.stopWheels();
        }, "autoLibMoveForwardCm"));
    }

    public void moveRightCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.rightEncoder();
        stateMachine.addState(new State(() -> {
            if (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) >= 5 * cmToClickRight) {
                wheelController.moveXY(speed, 0);
            } else {
                wheelController.moveXY(speed/5, 0);
            }
            boolean stop =(Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) <= 0.5 * cmToClickRight);
            if (debugMode) telemetry.addData("AutoLibStopWheels", stop);
            return stop;
        }, () -> {
            wheelController.stopWheels();
        }, "autoLibMoveRightCm"));
    }
}
