package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.State;

public class AutonomousLibrary extends Configurator {
    double cmToClickForward = 16.5;
    double cmToClickRight = 24.3;
    double turnDamping = 2;
    double slowDown = 5;
    double slowDist = 5;
    double stopDist = 0.5;

    public void moveForwardCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.avgEncoder();
        stateMachine.addState(new State(() -> {
            double turnAdjust = (wheelController.leftEncoder() - wheelController.rightEncoder())/turnDamping;
            if (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) >= slowDist * cmToClickForward) {
                wheelController.moveXYTurn(0, speed, turnAdjust);
            } else {
                wheelController.moveXYTurn(0, speed/slowDown, turnAdjust);
            }
            boolean stop = (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) <= stopDist * cmToClickForward);
            if (debugMode) telemetry.addData("AutoLibStopWheels", stop);
            return stop;
        }, () -> {
            wheelController.stopWheels();
        }, "autoLibMoveForwardCm"));
    }

    public void moveRightCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.rightEncoder();
        stateMachine.addState(new State(() -> {
            double turnAdjust = (wheelController.leftEncoder() - wheelController.rightEncoder())/turnDamping;
            if (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) >= slowDist * cmToClickRight) {
                wheelController.moveXYTurn(speed, 0, turnAdjust);
            } else {
                wheelController.moveXYTurn(speed/slowDown, 0, turnAdjust);
            }
            boolean stop =(Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) <= stopDist * cmToClickRight);
            if (debugMode) telemetry.addData("AutoLibStopWheels", stop);
            return stop;
        }, () -> {
            wheelController.stopWheels();
        }, "autoLibMoveRightCm"));
    }
}
