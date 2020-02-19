package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

public class DeadWheelOdometer {
    private Configurator config;
    private DcMotor leftOdometer;
    private DcMotor rightOdometer;
    private DcMotor centerOdometer;
    private PVector robotPos;
    private double robotRot;

    //Utility to find how much encoders have changed
    private double oldLeftEncoder = -1;
    private double oldRightEncoder = -1;
    private double oldBackLeftEncoder = -1;

    //Configure these to our wheels and wheelbase, measure in centimeters
    private PVector leftWheelPos = new PVector(14.5, 19.0);
    private PVector centerWheelPos = new PVector(-14.5, 19.0);
    private PVector rightWheelPos = new PVector(14.5, -19.0);
    double wheelRadius = 4.75;
    double wheelCircumference = (wheelRadius * 2) * Math.PI;
    double strafeEfficiency = 1;
    double forwardEfficiency = 1;

    //Configure this to our motors
    double ticksToDegrees = 0.79998;

    private void odometryLoop() {
        /* The math behind this code: (W for omega)
        If W_n is the rotation change for wheel n, r is the wheel radius, and K is |X of wheel n| + |Y of wheel n|,
        then we can find the robot's velocity V and rotation velocity W_v using

        r * (1/4W_1 + 1/4W_2 + 1/4W_3 + 1/4W_4)                =   V_x
        r * (-1/4W_1 + 1/4W_2 - 1/4W_3 + 1/4W_4)               =   V_y
        r * (-1/(4K)W_1 - 1/(4K)W_2 + 1/(4K)W_3 + 1/(4K)W_4)   =   W_v
        */

        double leftDistChange = ((oldLeftEncoder - leftOdometer.getCurrentPosition()) * ticksToDegrees) * wheelCircumference;
        double rightDistChange = ((oldRightEncoder - rightOdometer.getCurrentPosition()) * ticksToDegrees) * wheelCircumference;
        double centerDistChange = ((oldBackLeftEncoder - centerOdometer.getCurrentPosition()) * ticksToDegrees) * wheelCircumference;

        double localRobotRot;
        localRobotRot = 0;

        PVector localRobotPos;
        localRobotPos = new PVector(
                centerDistChange - (0),
                (leftDistChange + rightDistChange) / 2
        );

        if (config.getDebugMode()) config.telemetry.addLine("LocalPos: (" + Math.round(localRobotPos.x) + ", " + Math.round(localRobotPos.y) + ")");
        if (config.getDebugMode()) config.telemetry.addLine("LocalRot: " + Math.round(localRobotRot));

        //Convert to "field" coordinates
        localRobotPos = new PVector(-localRobotPos.y * strafeEfficiency, localRobotPos.x * forwardEfficiency);
        PVector rotated = localRobotPos.rotate(Math.toRadians(robotRot));

        //Add how the robot has moved to it's overall position
        setPos(PVector.add(robotPos, rotated));
        double addedRotation = robotRot - Math.toDegrees(localRobotRot);
        double posClippedRotation = addedRotation > 360 ? addedRotation - 360 : addedRotation;
        double clippedRotation = posClippedRotation < 0 ? posClippedRotation + 360 : posClippedRotation;
        setRot(clippedRotation);

        //Update all of the "old" values
        oldLeftEncoder = leftOdometer.getCurrentPosition();
        oldRightEncoder = rightOdometer.getCurrentPosition();
        oldBackLeftEncoder = centerOdometer.getCurrentPosition();
    }
    
    public DeadWheelOdometer(Configurator config) { //Store the config
        this.config = config;
    }

    public void beginOdometry() {

        //Update all of the "old" values
        oldLeftEncoder = leftOdometer.getCurrentPosition();
        oldRightEncoder = rightOdometer.getCurrentPosition();
        oldBackLeftEncoder = centerOdometer.getCurrentPosition();

        //Ad a state tu run odometry functions every tick
        State odometryState = new State(() -> {
            //Run both odometry loops
            odometryLoop();

            //Telemetry
            if (config.getDebugMode()) config.telemetry.addLine("Pos: (" + Math.round(robotPos.x) + ", " + Math.round(robotPos.y) + ")");
            if (config.getDebugMode()) config.telemetry.addLine("Rot: " + Math.round(robotRot));
            return false;
        }, () -> {}, "Odometry");

        config.stateMachine.addState(odometryState);
    }

    public PVector getPos() {
        return robotPos;
    }

    public double getRot() {
        return robotRot;
    }

    public void setPos(PVector pos) {
        robotPos = pos;
    }

    public void setRot(double rot) {
        robotRot = rot;
    }
}
