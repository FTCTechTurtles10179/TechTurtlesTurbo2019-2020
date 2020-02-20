package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.SingleState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="AutoTest")
public class AutoTest extends AutonomousLibrary {
    //Declare claw servo and armMotor
    Servo claw;
    Servo claw2;
    DcMotor armMotor;

    //Initialize armDownEncoder and armUpEncoder values
    int armDownEncoder = 0;
    int armUpEncoder = 1500;

    public void setupOpMode(){
        //Get the claw servos
        claw = getServo("claw");
        claw2 = getServo("claw2");

        armMotor = getDcMotor("armMotor");//Get the armMotor


        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);//Set encoder value of armMotor to 0
        armMotor.setTargetPosition(armDownEncoder);//Lower arm
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Open claw
        claw.setPosition(0);
        claw2.setPosition(1);

        //Initialize starting position and rotation
        PVector startingPos = new PVector(0,0);
        initializeOdometry(startingPos,90);

        PVector stone = new PVector(0, -50);

        State goToStone = new SingleState(() -> {//Creates a new SingleState, goToStone
            setTargetXYRot(stone, -90, State.blank());//Line up with stone and pass grabStone into the state machine
        }, "GoToStone");//Name the state GoToStone

        stateMachine.addState(goToStone);//Passes goToStone into the state machine, calling it
    }
}