package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

import java.util.ArrayList;

public abstract class AutonomousLibrary extends Configurator {
    private double cmToClickForward = 16.5;
    private double cmToClickRight = 24.3;
    private double slowDown = 3;
    private double slowDist = 15;
    private double stopDist = 5;
    private double fastStopDist = 10;
    private double speed = 0.3;

    ArrayList<PVector> targetPos = new ArrayList<>();
    ArrayList<Double> targetRot = new ArrayList<>();
    ArrayList<State> states = new ArrayList<>();
    MecanumOdometer odometry = new MecanumOdometer(this);

    public void initializeOdometry(PVector startingPos, double startingRot) {
        odometry.setPos(startingPos);
        odometry.setRot(startingRot);
        odometry.beginOdometry();

        stateMachine.addState(new State(() -> {
            updateWheelSpeed();
            return false;
        }, () -> {}, "AutoLibUpdateWheels"));
    }

    private void updateWheelSpeed() {
        PVector fieldMotion = PVector.sub(targetPos.get(0), odometry.getPos());
        double moveSpeed = fieldMotion.mag() > slowDist ? speed : (fieldMotion.mag()/slowDist) * speed;
        PVector botMotion = fieldMotion.normalize().rotate(Math.toRadians(odometry.getRot())).mult(moveSpeed);

        double fieldTurn = odometry.getRot() - targetRot.get(0);
        double turnSpeed = fieldTurn > slowDist ? speed : (fieldTurn/slowDist) * speed;
        double botTurn = Range.clip(fieldTurn, -turnSpeed, turnSpeed);

        wheelController.moveXY/*Turn*/(botMotion.x, botMotion.y/*, botTurn*/);

        if (PVector.dist(odometry.getPos(), targetPos.get(0)) < stopDist) {
            if (states.get(0).getStateName() != "Hidden") stateMachine.addState(states.get(0));
            states.remove(0);
            targetPos.remove(0);
            targetRot.remove(0);
        }
    }

    public void setTargetXYRot(PVector pos, double rot) {
        targetPos.add(pos);
        targetRot.add(rot);
        states.add(State.blank());
    }

    public void setTargetXYRot(PVector pos, double rot, State onFinished) {
        targetPos.add(pos);
        targetRot.add(rot);
        states.add(onFinished);
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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
