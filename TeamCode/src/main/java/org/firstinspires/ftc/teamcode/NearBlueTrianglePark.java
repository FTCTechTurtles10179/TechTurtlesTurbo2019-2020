package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.lib.AutonomousLibrary;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.StartState;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="NearBlueTrianglePark")
public class NearBlueTrianglePark extends AutonomousLibrary {

    @Override
    public void setupOpMode() {

        //Set starting position and rotation
        initializeOdometry(new PVector(342.9, 281.94), 270);

        //Initialize waypoints
        PVector underSkybridge = new PVector(96.06, 182.88);

        State strafeToBridge = new StartState(() -> {
            setTargetXYRot(underSkybridge, 270);
        }, () -> true, () -> {
        }, "StrafeRightToBridge");

        stateMachine.addState(strafeToBridge); //After we setup the states, add the first one to our stateMachine
    }
}
