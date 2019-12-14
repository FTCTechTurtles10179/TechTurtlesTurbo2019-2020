package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.states.StartState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearBlueTriangle")
public class NearBlueTriangle extends AutonomousLibrary {
    Servo foundationGrabber;

    @Override
    public void setupOpMode() {
        foundationGrabber = getServo("foundationGrabber"); //Get the foundation grabber servo
        foundationGrabber.setPosition(1);
        debugMode = true; //Give telemetry of the running states, encoders, autonomous library, etc.

        State strafeRightToBridge = new StartState(() -> {
            moveRightCentimeters(-135, -1);
        }, () -> busyMoving, () -> {}, 700, "StrafeRightToBridge");

        State releasePlatform = new State(() -> {
            foundationGrabber.setPosition(0);
            return false;
        }, () -> stateMachine.addState(strafeRightToBridge), 700, "ReleasePlatform");

        State moveBackwardFromPlatform = new StartState(() -> moveForwardCentimeters(73, 1), () -> busyMoving, () -> stateMachine.addState(releasePlatform), 700, "MoveBackwardFromPlatform");

        State grabPlatform = new State(() -> {
            foundationGrabber.setPosition(0);
            return false;
        }, () -> stateMachine.addState(moveBackwardFromPlatform), 700, "GrabPlatform");

        State moveForwardToPlatform = new StartState(() -> moveForwardCentimeters(-73, -1), () -> busyMoving, () -> stateMachine.addState(grabPlatform), "MoveForwardToPlatform");

        State strafeAlign = new StartState(() -> moveRightCentimeters(40.5, 1), () -> busyMoving, () -> stateMachine.addState(grabPlatform), "StrafeAlign");

        stateMachine.addState(moveForwardToPlatform);
    }
}