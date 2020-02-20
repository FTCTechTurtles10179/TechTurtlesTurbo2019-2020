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
    Servo foundationGrabber2;

    @Override
    public void setupOpMode() {
        foundationGrabber = getServo("foundationGrabber"); //Get the foundation grabber servo
        foundationGrabber2 = getServo("foundationGrabber2");
        foundationGrabber.setPosition(1);
        foundationGrabber2.setPosition(0);

        //Set starting position and rotation
        initializeOdometry(new PVector(342.9, 281.94), 90);

        //Initialize waypoints
        PVector foundation = new PVector(266.7,311.76);
        PVector loadingZone = new PVector(342.9,311.76);
        PVector underSkybridge = new PVector(269.7, 182.88);

        State strafeToBridge = new StartState(() -> {
            setTargetXYRot(underSkybridge, 90);
        }, () -> true, () -> {}, "StrafeRightToBridge");

        State releasePlatform = new State(() -> {
            //Pull up the foundation grabber servo
            foundationGrabber.setPosition(1);
            foundationGrabber2.setPosition(0);
            return false;
        }, () -> { //When the state is done
            stateMachine.addState(strafeToBridge); //Run this state
        }, 1000, "ReleasePlatform");

        State pullBackFoundation = new StartState(() -> {
            setTargetXYRot(loadingZone, 90, releasePlatform);
        }, () -> true, () -> {},"MoveBackwardFromPlatform");

        State grabPlatform = new State(() -> {
            //Push down the foundation grabber servo
            foundationGrabber.setPosition(0);
            foundationGrabber2.setPosition(1);
            return false;
        }, () -> { //When the state is done
            stateMachine.addState(pullBackFoundation); //Run this state
        }, 1000, "GrabPlatform");

        State moveToPlatform = new StartState(() -> {
            setTargetXYRot(foundation, 90, grabPlatform);
        }, () -> true, () -> {}, "MoveForwardToPlatform");

        stateMachine.addState(moveToPlatform); //After we setup the states, add the first one to our stateMachine
    }
}