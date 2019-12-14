package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.states.State;

public abstract class AutonomousLibrary extends Configurator {
    private double cmToClickForward = 16.5;
    private double cmToClickRight = 24.3;
    private double turnDamping = 2;
    private double slowDown = 5;
    private double slowDist = 5;
    private double stopDist = 0.5;
    public boolean busyMoving = false;

    public void moveForwardCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.avgEncoder();
        if (!busyMoving) {
            stateMachine.addState(new State(() -> {
                busyMoving = true;
                double turnAdjust = (wheelController.leftEncoder() - wheelController.rightEncoder()) / turnDamping;
                if (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) >= slowDist * cmToClickForward) {
                    wheelController.moveXYTurn(0, speed, turnAdjust);
                } else {
                    wheelController.moveXYTurn(0, speed / slowDown, turnAdjust);
                }
                boolean stop = (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) <= stopDist * cmToClickForward);
                if (debugMode) telemetry.addData("AutoLibStopWheels", stop);
                return stop;
            }, () -> {
                wheelController.stopWheels();
                busyMoving = false;
            }, "autoLibMoveForwardCm"));
        }
    }

    public void moveRightCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.rightEncoder();
        if (!busyMoving) {
            stateMachine.addState(new State(() -> {
                busyMoving = true;
                double turnAdjust = (wheelController.rightEncoder() - backRight.getCurrentPosition()) / turnDamping;
                if (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) >= slowDist * cmToClickRight) {
                    wheelController.moveXYTurn(speed, 0, turnAdjust);
                } else {
                    wheelController.moveXYTurn(speed / slowDown, 0, turnAdjust);
                }
                boolean stop = (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) <= stopDist * cmToClickRight);
                if (debugMode) telemetry.addData("AutoLibStopWheels", stop);
                return stop;
            }, () -> {
                busyMoving = false;
                wheelController.stopWheels();
            }, "autoLibMoveRightCm"));
        }
    }
}
