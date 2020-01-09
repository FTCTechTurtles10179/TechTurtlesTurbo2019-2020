package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.states.SingleState;
import org.firstinspires.ftc.teamcode.lib.util.states.StartState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearBlueSquareStackless")
public class NearBlueSquareStackless extends AutonomousLibrary {
    Servo claw;

    public void setupOpMode(){
        claw = getServo("claw"); //Get the foundation grabber servo
        claw.setPosition(1);

        State releaseStone = new SingleState(() ->
            claw.setPosition(0)
        , "ReleaseStone");

        State strafeRightToSkybridge = new SingleState(() ->
            moveRightCentimeters(100,0.75, releaseStone)
        ,"StrafeRightToSkybridge");

        State moveBackFromStones = new SingleState(() ->
            moveForwardCentimeters(20, -0.75, strafeRightToSkybridge)
        , "MoveBackFromStones");

        State grabStone = new State(() -> {
            claw.setPosition(1);
            return false;
        }, () ->
            stateMachine.addState(moveBackFromStones), 2000, "GrabStone");

        State moveForwardToStone = new SingleState(() ->
            moveForwardCentimeters(75,0.5, grabStone)
        , "MoveForwardToStone");

        State strafeRightToAlign = new SingleState(() ->
            moveRightCentimeters(15,0.5,moveForwardToStone)
        , "StrafeRightToAlign");

        State openClaw = new State(() -> {
            claw.setPosition(0);
            return false;
        }, () ->
            stateMachine.addState(strafeRightToAlign)
        , 2000, "OpenClaw");

        stateMachine.addState(openClaw);
    }
}