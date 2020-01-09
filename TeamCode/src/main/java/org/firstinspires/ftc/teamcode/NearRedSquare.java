package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.states.SingleState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearRedSquare")
public class NearRedSquare extends AutonomousLibrary {
    Servo claw;

    @Override
    public void setupOpMode() {
        claw = getServo("claw"); //Get the foundation grabber servo
        claw.setPosition(1);

        State releaseStone = new SingleState(() ->
                claw.setPosition(0)
                , "ReleaseStone");

        State strafeLeftToFoundation = new SingleState(() ->
                moveRightCentimeters(169,-0.5, releaseStone)
                ,"StrafeRightToSkybridge");

        State moveBackFromStones = new SingleState(() ->
                moveForwardCentimeters(3, -0.5, strafeLeftToFoundation)
                , "MoveBackFromStones");

        State grabStone = new State(() -> {
            claw.setPosition(1);
            return false;
        }, () ->
                stateMachine.addState(moveBackFromStones), 2000, "GrabStone");

        State moveForwardToStone = new SingleState(() ->
                moveForwardCentimeters(75,0.5, grabStone)
                , "MoveForwardToStone");

        State strafeLeftToAlign = new SingleState(() ->
                moveRightCentimeters(7,-0.5,moveForwardToStone)
                , "StrafeRightToAlign");

        State openClaw = new State(() -> {
            claw.setPosition(0);
            return false;
        }, () ->
                stateMachine.addState(strafeLeftToAlign)
                , 2000, "OpenClaw");

        stateMachine.addState(openClaw);
    }
}
