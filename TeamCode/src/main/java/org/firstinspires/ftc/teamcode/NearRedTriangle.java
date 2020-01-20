package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.StartState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearRedTriangle")
public class NearRedTriangle extends AutonomousLibrary { //Note: the states are backwards, the one at the end runs first
    Servo foundationGrabber;

    @Override
    public void setupOpMode() {
        foundationGrabber = getServo("foundationGrabber"); //Get the foundation grabber servo
        foundationGrabber.setPosition(1);

        initializeOdometry(new PVector(346.76, 265.76), 90);

        State strafeToBridge = new StartState(() -> {
            setTargetXYRot(new PVector(340.76, 175.5), 90);
        }, () -> true, () -> {}, "StrafeRightToBridge");

        State releasePlatform = new State(() -> {
            foundationGrabber.setPosition(1); //Pull up the foundation grabber servo
            return false;
        }, () -> { //When the state is done
            stateMachine.addState(strafeToBridge); //Run this state
        }, 1000, "ReleasePlatform");

        State pullBackFoundation = new StartState(() -> {
            setTargetXYRot(new PVector(340.76, 265.76), 90, releasePlatform);
        }, () -> true, () -> {},"MoveBackwardFromPlatform");

        State grabPlatform = new State(() -> {
            foundationGrabber.setPosition(0); //Push down the foundation grabber servo
            return false;
        }, () -> { //When the state is done
            stateMachine.addState(pullBackFoundation); //Run this state
        }, 1000, "GrabPlatform");

        State moveToPlatform = new StartState(() -> {
            setTargetXYRot(new PVector(216.76, 212.76), 90, grabPlatform);
        }, () -> true, () -> {}, "MoveForwardToPlatform");

        stateMachine.addState(moveToPlatform); //After we setup the states, add the first one to our stateMachine
    }
}