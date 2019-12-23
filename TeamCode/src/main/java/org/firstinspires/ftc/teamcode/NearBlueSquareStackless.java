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

        State releaseStone = new SingleState(() -> {
            claw.setPosition(0);
        }, "ReleaseStone");

        State moveForwardToSkybridge = new SingleState(() -> {
            moveForwardCentimeters(100, 0.75, releaseStone);
        }, "MoveForwardToSkybridge");

        State turnLeftTowardSkybridge = new State(() -> {
            wheelController.moveTurn(1);
            return false;
        }, () -> {
            wheelController.stopWheels();
            stateMachine.addState(moveForwardToSkybridge);
        }, 1000, "TurnLeftTowardSkybridge");

        State grabStone = new State(() -> {
            claw.setPosition(1);
            return false;
        }, () -> {
            stateMachine.addState(turnLeftTowardSkybridge);
        }, 2000, "GrabStone");

        State moveForwardToStone = new SingleState(() -> {
            moveForwardCentimeters(80,0.5, grabStone);
        }, "MoveForwardToStone");

        State openClaw = new State(() -> {
            claw.setPosition(0);
            return false;
        }, () -> {
            stateMachine.addState(moveForwardToSkybridge);
        }, 2000, "OpenClaw");

        stateMachine.addState(moveForwardToStone);
    }
}
