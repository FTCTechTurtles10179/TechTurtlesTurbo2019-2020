package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.states.State;

public abstract class AutonomousLibrary extends Configurator {
    private double cmToClickForward = 16.5;
    private double cmToClickRight = 24.3;
    private double slowDown = 3;
    private double slowDist = 5;
    private double stopDist = 0.5;

    public void moveForwardCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.avgEncoder();
        stateMachine.addState(new State(() -> {
            double turnAdjust = 0;//(Math.abs(wheelController.rightEncoder()) - Math.abs(wheelController.leftEncoder())) / turnDamping;
            if (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) >= slowDist * cmToClickForward) {
                wheelController.moveXY(0, speed);
            } else {
                wheelController.moveXY(0, speed / slowDown);
            }
            boolean stop = (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) <= stopDist * cmToClickForward);
            if (getDebugMode()) telemetry.addData("AutoLibStopWheels", stop);
            return stop;
        }, () -> {
            wheelController.stopWheels();
        }, "autoLibMoveForwardCm"));
    }

    public void moveRightCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.rightEncoder();
        stateMachine.addState(new State(() -> {
            double turnAdjust = 0;//(Math.abs(wheelController.rightEncoder()) - Math.abs(backRight.getCurrentPosition())) / turnDamping;
            if (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) >= slowDist * cmToClickRight) {
                wheelController.moveXY(speed, 0);
            } else {
                wheelController.moveXY(speed / slowDown, 0);
            }
            boolean stop = (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) <= stopDist * cmToClickRight);
            if (getDebugMode()) telemetry.addData("AutoLibStopWheels", stop);
            return stop;
        }, () -> {
            wheelController.stopWheels();
        }, "autoLibMoveRightCm"));
    }

    public void moveForwardCentimeters(double distance, double speed, State runOnStop) {
        double startingEncoder = wheelController.avgEncoder();
        stateMachine.addState(new State(() -> {
            double turnAdjust = 0;//(Math.abs(wheelController.rightEncoder()) - Math.abs(wheelController.leftEncoder())) / turnDamping;
            if (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) >= slowDist * cmToClickForward) {
                wheelController.moveXY(0, speed);
            } else {
                wheelController.moveXY(0, speed / slowDown);
            }
            boolean stop = (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) <= stopDist * cmToClickForward);
            if (getDebugMode()) telemetry.addData("AutoLibStopWheels", stop);
            return stop;
        }, () -> {
            wheelController.stopWheels();
            stateMachine.addState(runOnStop);
        }, "autoLibMoveForwardCm"));
    }

    public void moveRightCentimeters(double distance, double speed, State runOnStop) {
        double startingEncoder = wheelController.rightEncoder();
        stateMachine.addState(new State(() -> {
            double turnAdjust = 0;//(Math.abs(wheelController.rightEncoder()) - Math.abs(backRight.getCurrentPosition())) / turnDamping;
            if (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) >= slowDist * cmToClickRight) {
                wheelController.moveXY(speed, 0);
            } else {
                wheelController.moveXY(speed / slowDown, 0);
            }
            boolean stop = (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) <= stopDist * cmToClickRight);
            if (getDebugMode()) telemetry.addData("AutoLibStopWheels", stop);
            return stop;
        }, () -> {
            wheelController.stopWheels();
            stateMachine.addState(runOnStop);
        }, "autoLibMoveRightCm"));
    }
}
