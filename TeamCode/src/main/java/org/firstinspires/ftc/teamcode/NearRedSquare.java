package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.states.SingleState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearRedSquare")
public class NearRedSquare extends AutonomousLibrary {

    //Declare claw servo and armMotor
    Servo claw;
    DcMotor armMotor;

    //Initialize armDownEncoder and armUpEncoder values
    int armDownEncoder = 0;
    int armUpEncoder = 1500;

    @Override
    public void setupOpMode() {
        claw = getServo("claw");//Get the claw servo
        armMotor = getDcMotor("armMotor");//Get the armMotor
        claw.setPosition(0);//Open claw
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Set encoder value of armMotor to 0
        armMotor.setTargetPosition(armDownEncoder);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        State strafeRightUnderSkybridge = new SingleState(() -> {
            moveRightCentimeters(106.25, 0.5);
        }, "StrafeRightUnderSkybridge"
        );

        State releaseStone = new State(() -> {
            claw.setPosition(0);
            return false;
        }, () -> {
            stateMachine.addState(strafeRightUnderSkybridge);
        }, "ReleaseStone");

        State strafeLeftToFoundation = new SingleState(() -> {
            moveRightCentimeters(-169, -0.5, releaseStone);
        },"StrafeRightToSkybridge");

        State moveBackFromStones = new SingleState(() -> {
            moveForwardCentimeters(-40, -0.5, strafeLeftToFoundation);
        }, "MoveBackFromStones");

        State grabStone = new State(() -> {
            claw.setPosition(1);
            return false;
        }, () -> {
            stateMachine.addState(moveBackFromStones);
        }, 2000, "GrabStone");//Name the state GrabStone and run for 2 seconds

        State moveForwardToStone = new SingleState(() -> {//Creates a new SingleState, moveForwardToStone
            //Move forward to the stone and pass grabStone into the state machine
            moveForwardCentimeters(75, 0.5, grabStone);
        }, "MoveForwardToStone");//Name the state MoveForwardToStone

        State strafeLeftToAlign = new SingleState(() -> {//Creates a new SingleState, strafeLeftToAlign
            //Strafe left slightly to align with the center stone in the right 3 stones
            //Pass moveForwardToStone into the state machine
            moveRightCentimeters(-7, -0.5, moveForwardToStone);
        }, "StrafeRightToAlign");//Name the state StrafeLeftToAlign

        stateMachine.addState(strafeLeftToAlign);//Passes strafeLeftToAlign into the state machine, calling it
    }
}
