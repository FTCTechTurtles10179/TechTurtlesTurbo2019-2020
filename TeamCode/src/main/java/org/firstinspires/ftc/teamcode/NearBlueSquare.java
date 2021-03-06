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
    DcMotor armMotor;

    //Initialize armDownEncoder and armUpEncoder values
    int armDownEncoder = 0;
    int armUpEncoder = 1500;

    public void setupOpMode(){
        claw = getServo("claw");//Get the claw servo
        armMotor = getDcMotor("armMotor");//Get the armMotor

        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Set encoder value of armMotor to 0
        armMotor.setTargetPosition(armDownEncoder);//Lower arm
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        claw.setPosition(0);//Open claw

        //Initialize starting position and rotation
        PVector startingPos = new PVector(0,0);
        initializeOdometry(startingPos,-90);

        //Set waypoints
        PVector stone = new PVector(-75,7);
        PVector backFromStones = new PVector(-55,7);
        PVector foundation = new PVector(-75,169);
        PVector underSkybridge = new PVector(-55, 122);

        State strafeLeftUnderSkybridge = new SingleState(() -> {//Creates a new SingleState, strafeLeftUnderSkybridge
            //Strafe left to Navigate
            setTargetXYRot(underSkybridge, 0);
        }, "StrafeLeftUnderSkybridge");//Name the state StrafeLeftUnderSkybridge

        State releaseStone = new State(() -> {//Creates a new state, releaseStone
            claw.setPosition(0);//Deliver the stone
            armMotor.setTargetPosition(armDownEncoder);//Lower arm
            return false;
        }, () -> {
            stateMachine.addState(strafeLeftUnderSkybridge);//Pass strafeLeftUnderSkybridge into the state machine
        },"ReleaseStone");//Name the state ReleaseStone

        State goToFoundation = new State(() -> {//Creates a new State, goToFoundation
            setTargetXYRot(backFromStones, 0);//Move back from the stones to avoid collision with the skybridge
            setTargetXYRot(foundation,0,releaseStone);//Go to foundation and passes releaseStone into the state machine
            return false;
        },() -> {}, "GoToFoundation");//Name the state GoToFoundation

        State grabStone = new State(() -> {//Creates a new state, grabStone
            claw.setPosition(1);//Grab the stone
            armMotor.setTargetPosition(armUpEncoder);//Raise arm for easier transport
            return false;
        }, () -> {
            stateMachine.addState(goToFoundation);//Pass moveBackFromStones into the state machine
        }, 2000, "GrabStone");//Name the state GrabStone and run for 2 seconds

        State goToStone = new SingleState(() -> {//Creates a new SingleState, goToStone
            setTargetXYRot(stone, 0, grabStone);//Line up with stone and pass grabStone into the state machine
        }, "StrafeRightToAlign");//Name the state GoToStone

        stateMachine.addState(goToStone);//Passes goToStone into the state machine, calling it
    }
}
