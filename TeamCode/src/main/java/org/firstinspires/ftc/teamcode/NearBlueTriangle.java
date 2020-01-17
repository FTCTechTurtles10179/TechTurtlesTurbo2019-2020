package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.StartState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearBlueTriangle")
public class NearBlueTriangle extends AutonomousLibrary {
    Servo foundationGrabber;

    @Override
    public void setupOpMode() {
        foundationGrabber = getServo("foundationGrabber"); //Get the foundation grabber servo
        foundationGrabber.setPosition(1);

        initializeOdometry(new PVector(22.86, 265.76), -90);

        State strafeToBridge = new StartState(() -> {
            setTargetXYRot(new PVector(24.86, 230), -90);
            setTargetXYRot(new PVector(32.86, 175.5), -90);
        }, () -> true, () -> {}, "StrafeLeftToBridge");

        State releasePlatform = new State(() -> {
            foundationGrabber.setPosition(1); //Pull up the foundation grabber servo
            return false;
        }, () -> { //When the state is done
            stateMachine.addState(strafeToBridge); //Run this state
        }, 1000, "ReleasePlatform");

        State pullBackFoundation = new StartState(() -> {
            setTargetXYRot(new PVector(344.76, 265.76), -90, releasePlatform);
        }, () -> true, () -> {},"MoveBackwardFromPlatform");

        State grabPlatform = new State(() -> {
            foundationGrabber.setPosition(0); //Push down the foundation grabber servo
            return false;
        }, () -> { //When the state is done
            stateMachine.addState(pullBackFoundation); //Run this state
        }, 1000, "GrabPlatform");

        State moveToPlatform = new StartState(() -> {
            setTargetXYRot(new PVector(32.86, 265.76), -90);
            setTargetXYRot(new PVector(152.86, 212.76), -90, grabPlatform);
        }, () -> true, () -> {}, "MoveForwardToPlatform");

        stateMachine.addState(moveToPlatform); //After we setup the states, add the first one to our stateMachine
    }
}