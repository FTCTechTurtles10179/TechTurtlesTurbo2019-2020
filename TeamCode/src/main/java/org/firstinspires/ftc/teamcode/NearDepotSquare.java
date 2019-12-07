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
        stateMachine.debugMode = true; //Give telemetry of the running states

        State moveToSkybridge = new State(() -> { //Make a new state, and while it's running
            //move to the skybridge
            moveRightCentimeters(-70, -0.5);
            return false;
        }, () -> {}, 0, "moveToSkybridge"); //Run once (0 milliseconds) and name it moveToSkybridge

        stateMachine.addState(moveToSkybridge);
    }
}
