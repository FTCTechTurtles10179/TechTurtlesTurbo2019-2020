package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.State;

@Autonomous(name="NearDepotSquare")
public class NearDepotSquare extends AutonomousLibrary {
    Servo foundationGrabber;

    public void setupOpMode(){
        foundationGrabber = getServo("foundationGrabber"); //Get the foundation grabber servo
        foundationGrabber.setPosition(1);
        debugMode = true; //Give telemetry of the running states, encoders, autonomous library, etc.

        stateMachine.addState(new State(() -> { //Make a new state, and while it's running
            //move to the skybridge
            moveRightCentimeters(-70, -1);
            return true; //Only run once, return true to remove the state from the statemachine.
        }, () -> {}, "moveToSkybridge")); //Name it moveToSkybridge
    }
}
