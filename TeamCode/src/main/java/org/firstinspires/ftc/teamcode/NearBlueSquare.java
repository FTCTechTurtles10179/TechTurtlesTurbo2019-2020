package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.states.StartState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearBlueSquare")
public class NearBlueSquare extends AutonomousLibrary {
    Servo foundationGrabber;

    public void setupOpMode(){
        foundationGrabber = getServo("foundationGrabber"); //Get the foundation grabber servo
        foundationGrabber.setPosition(1);

        State moveForwardToStone = new StartState(() -> {
            moveForwardCentimeters(55, 0.5);
        }, () -> true, () -> {}, "MoveForwardToStone");
        State grabStone = new State(() -> {
            foundationGrabber.setPosition(1);
            return false;
        }, () -> {
            stateMachine.addState(moveForwardToStone);
        }, 1000, "GrabStone");
        State turnLeft = new StartState(() -> {
            wheelController.moveTurn(1);
        }, () -> true, () -> {}, "StrafeRightToBridge");
    }
}
