package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.SingleState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearBlueSquare")
public class NearBlueSquare extends AutonomousLibrary {
    //Declare claw servo and armMotor
    Servo claw;
    Servo claw2;
    Servo foundationGrabber;
    Servo foundationGrabber2;
    DcMotor armMotor;

    //Initialize armDownEncoder and armUpEncoder values
    int armDownEncoder = 0;
    int armUpEncoder = 1500;

    public void setupOpMode(){
        //Get the claw servos
        claw = getServo("claw");
        claw2 = getServo("claw2");

        //Get the foundationGrabber servos
        foundationGrabber = getServo("foundationGrabber");
        foundationGrabber2 = getServo("foundationGrabber2");

        armMotor = getDcMotor("armMotor");//Get the armMotor

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Set encoder value of armMotor to 0
        armMotor.setTargetPosition(armDownEncoder);//Lower arm
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Open claw
        claw.setPosition(0);
        claw2.setPosition(1);

        //Open foundationGrabber
        foundationGrabber.setPosition(0);
        foundationGrabber2.setPosition(1);

        //Initialize starting position and rotation
        PVector startingPos = new PVector(342.9,83.82);
        initializeOdometry(startingPos,270);

        //Set waypoints
        PVector stone = new PVector(266.7,91.44);
        PVector backFromStones = new PVector(269.7,91.44);
        PVector foundation = new PVector(266.7,311.76);
        PVector loadingZone = new PVector(342.9,311.76);
        PVector underSkybridge = new PVector(269.7, 182.88);

        State strafeRightUnderSkybridge = new SingleState(() -> {//Creates a new SingleState, strafeRightUnderSkybridge
            //Strafe right to Navigate
            setTargetXYRot(underSkybridge, 270);
        }, "StrafeRightUnderSkybridge");//Name the state StrafeRightUnderSkybridge

        State moveBackToLoadingZone = new State(() -> {//Creates a new State, moveBackToLoadingZone
            setTargetXYRot(loadingZone, 90);

            //Release foundation
            foundationGrabber.setPosition(0);
            foundationGrabber2.setPosition(1);

            return false;
        }, () -> {
            stateMachine.addState(strafeRightUnderSkybridge);
        }, "MoveBackToLoadingZone");

        State turnAroundToFoundation = new State(() -> {
            //Turn around to present the foundation grabber to the foundation
            setTargetXYRot(foundation, 90);

            //Grab foundation
            foundationGrabber.setPosition(1);
            foundationGrabber2.setPosition(0);

            return false;
        }, () -> {
            stateMachine.addState(moveBackToLoadingZone);
        }, "TurnAround");

        State releaseStone = new State(() -> {//Creates a new state, releaseStone
            //Deliver the stone
            claw.setPosition(0);
            claw2.setPosition(1);

            armMotor.setTargetPosition(armDownEncoder);//Lower arm
            return false;
        }, () -> {
            stateMachine.addState(turnAroundToFoundation);//Pass strafeLeftUnderSkybridge into the state machine
        },"ReleaseStone");//Name the state ReleaseStone

        State goToFoundation = new State(() -> {//Creates a new State, goToFoundation
            setTargetXYRot(backFromStones, 270);//Move back from the stones to avoid collision with the skybridge
            setTargetXYRot(foundation,270,releaseStone);//Go to foundation and passes releaseStone into the state machine
            return false;
        },() -> {}, "GoToFoundation");//Name the state GoToFoundation

        State grabStone = new State(() -> {//Creates a new state, grabStone
            //Grab the stone
            claw.setPosition(1);
            claw2.setPosition(0);

            armMotor.setTargetPosition(armUpEncoder);//Raise arm for easier transport
            return false;
        }, () -> {
            stateMachine.addState(goToFoundation);//Pass moveBackFromStones into the state machine
        }, 2000, "GrabStone");//Name the state GrabStone and run for 2 seconds

        State goToStone = new SingleState(() -> {//Creates a new SingleState, goToStone
            setTargetXYRot(stone, 270, grabStone);//Line up with stone and pass grabStone into the state machine
        }, "StrafeRightToAlign");//Name the state GoToStone

        stateMachine.addState(goToStone);//Passes goToStone into the state machine, calling it
    }
}
