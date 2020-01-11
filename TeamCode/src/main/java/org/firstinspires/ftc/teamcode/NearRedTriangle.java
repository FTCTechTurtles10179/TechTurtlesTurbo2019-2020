package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.states.StartState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearRedTriangle")
public class NearRedTriangle extends AutonomousLibrary {
    Servo foundationGrabber;

    @Override
    public void setupOpMode() {
        foundationGrabber = getServo("foundationGrabber"); //Get the foundation grabber servo
        foundationGrabber.setPosition(1);

        State strafeRightToBridge = new StartState(() -> {
            moveRightCentimeters(120, 0.5); //Strafe  right 120cm at 0.5 speed, or to the bridge
        }, () -> true, () -> {}, "StrafeRightToBridge");

        State releasePlatform = new State(() -> {
            foundationGrabber.setPosition(1); //Pull up the foundation grabber servo
            return false;
        }, () -> { //When the state is done
            stateMachine.addState(strafeRightToBridge); //Run this state
        }, 1000, "ReleasePlatform");

        State moveBackwardFromPlatform = new StartState(() -> {
            moveForwardCentimeters(110, 0.5, releasePlatform);
        }, () -> true, () -> {},"MoveBackwardFromPlatform");

        State grabPlatform = new State(() -> {
            foundationGrabber.setPosition(0);
            return false;
        }, () -> {
            stateMachine.addState(moveBackwardFromPlatform);
        }, 1000, "GrabPlatform");

        State moveForwardToPlatform = new StartState(() -> {
            moveForwardCentimeters(-70, -0.5, grabPlatform);
        }, () -> true, () -> {}, "MoveForwardToPlatform");

        State strafeAlign = new StartState(() -> {
            wheelController.moveXY(0, 0.1);
            moveRightCentimeters(-40.5, -0.5, moveForwardToPlatform);
        }, () -> true, () -> {}, "StrafeAlign");

        State moveOffWall = new StartState(() -> {
            moveForwardCentimeters(-8, -0.5, strafeAlign);
        }, () -> true, () -> {}, "MoveForwardToPlatform");

        stateMachine.addState(moveOffWall);
    }
}