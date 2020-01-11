package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.states.SingleState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearBlueSquareStackless")
public class NearBlueSquareStackless extends AutonomousLibrary {
    //Declare claw servo and armMotor
    Servo claw;
    DcMotor armMotor;

    //Initialize armDownEncoder and armUpEncoder values
    int armDownEncoder = 0;
    int armUpEncoder = 1500;

    public void setupOpMode(){
        claw = getServo("claw");//Get the claw servo
        armMotor = getDcMotor("armMotor");//Get the armMotor
        claw.setPosition(0);//Open claw
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Set encoder value of armMotor to 0
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setTargetPosition(armDownEncoder);//Lower arm

        State strafeLeftUnderSkybridge = new SingleState(() -> {//Creates a new SingleState, strafeLeftUnderSkybridge
            //Strafe left to Navigate
            moveRightCentimeters(-20, -0.5);
        }, "StrafeLeftUnderSkybridge");//Name the state StrafeLeftUnderSkybridge

        State releaseStone = new State(() -> {//Creates a new state, releaseStone
            claw.setPosition(0);//Deliver the stone
            armMotor.setTargetPosition(armDownEncoder);//Lower arm
            return false;
        }, () -> {
            stateMachine.addState(strafeLeftUnderSkybridge);//Pass strafeLeftUnderSkybridge into the state machine
        },"ReleaseStone");//Name the state ReleaseStone

        State strafeRightPastSkybridge = new SingleState(() -> {//Creates a new SingleState, strafeRightPastSkybridge
            //Strafe right past the Skybridge and pass releaseStone into the state machine
            moveRightCentimeters(135,0.5, releaseStone);
        },"StrafeRightPastSkybridge");//Name the state StrafeRightPastSkybridge

        State moveBackFromStones = new SingleState(() -> {//Creates a new SingleState, moveBackFromStones
            //Move back from the stones and pass strafeRightPastSkybridge into the state machine
            moveForwardCentimeters(-20, -0.5, strafeRightPastSkybridge);
        }, "MoveBackFromStones");//Name the state MoveBackFromStones

        State grabStone = new State(() -> {//Creates a new state, grabStone
            claw.setPosition(1);//Grab the stone
            armMotor.setTargetPosition(armUpEncoder);//Raise arm for easier transport
            return false;
        }, () -> {
            stateMachine.addState(moveBackFromStones);//Pass moveBackFromStones into the state machine
        }, 2000, "GrabStone");//Name the state GrabStone and run for 2 seconds


        State moveForwardToStone = new SingleState(() -> {//Creates a new SingleState, moveForwardToStone
            //Move forward to the stone and pass grabStone into the state machine
            moveForwardCentimeters(75, 0.5, grabStone);
        }, "MoveForwardToStone");//Name the state MoveForwardToStone

        State strafeRightToAlign = new SingleState(() -> {//Creates a new SingleState, strafeRightToAlign
            //Strafe right slightly to align with the center stone in the right 3 stones
            //Pass moveForwardToStone into the state machine
            moveRightCentimeters(7, 0.5, moveForwardToStone);
        }, "StrafeRightToAlign");//Name the state StrafeRightToAlign

        stateMachine.addState(strafeRightToAlign);//Passes strafeRightToAlign into the state machine, calling it
    }
}