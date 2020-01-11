package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

public class MecanumOdometer { //IMPORTANT!!!!! When configuring +Y is left, +X is forward! When reading values, +X is right +Y is forward!
    private Configurator config;
    private PVector robotPos;
    private double robotRot;

    //Utility to find how much encoders have changed
    private double oldFrontLeftEncoder = -1;
    private double oldFrontRightEncoder = -1;
    private double oldBackLeftEncoder = -1;
    private double oldBackRightEncoder = -1;

    //Configure these to our wheels and wheelbase, measure in centimeters
    private PVector frontLeftWheelPos = new PVector(14.5, 19.0);
    private PVector backLeftWheelPos = new PVector(-14.5, 19.0);
    private PVector frontRightWheelPos = new PVector(14.5, -19.0);
    private PVector backRightWheelPos = new PVector(-14.5, -19.0);
    double wheelRadius = 4.75;

    //Configure this to our motors
    double ticksToDegrees = 48.0/360.0;

    //K is used for math later, it is a bit off when directly calculated so it is an adjustable constant
    final double K = 5.5833;

    private void odometryLoop() {
        if (config.getDebugMode()) config.telemetry.addLine("Odometry active.");

        /* The math behind this code: (W for omega)
        If W_n is the rotation change for wheel n, r is the wheel radius, and K is |X of wheel n| + |Y of wheel n|,
        then we can find the robot's velocity V and rotation velocity W_v using
        
        r * (1/4W_1 + 1/4W_2 + 1/4W_3 + 1/4W_4)                =   V_x
        r * (-1/4W_1 + 1/4W_2 - 1/4W_3 + 1/4W_4)               =   V_y
        r * (-1/(4K)W_1 - 1/(4K)W_2 + 1/(4K)W_3 + 1/(4K)W_4)   =   W_v
        */

        double frontLeftDegreeChange = Math.toRadians((oldFrontLeftEncoder - config.frontLeft.getCurrentPosition()) * ticksToDegrees);
        double frontRightDegreeChange = Math.toRadians((oldFrontRightEncoder - config.frontRight.getCurrentPosition()) * ticksToDegrees);
        double backLeftDegreeChange = Math.toRadians((oldBackLeftEncoder - config.backLeft.getCurrentPosition()) * ticksToDegrees);
        double backRightDegreeChange = Math.toRadians((oldBackRightEncoder - config.backRight.getCurrentPosition()) * ticksToDegrees);

        PVector localRobotPos;
        localRobotPos = new PVector(
                wheelRadius * (0.25 * (frontLeftDegreeChange + frontRightDegreeChange + backLeftDegreeChange + backRightDegreeChange)),
                wheelRadius * ((0.25 * (frontRightDegreeChange + backLeftDegreeChange)) - (0.25 * (frontLeftDegreeChange + backLeftDegreeChange)))
        );

        double localRobotRot;
        localRobotRot = wheelRadius * ((1/(4.0 * K) * (backRightDegreeChange + frontRightDegreeChange)) - (1/(4.0 * K) * (backLeftDegreeChange + frontLeftDegreeChange)));

        if (config.getDebugMode()) config.telemetry.addLine("LocalPos: (" + localRobotPos.x + ", " + localRobotPos.y + ")");
        if (config.getDebugMode()) config.telemetry.addLine("LocalRot: " + localRobotRot);

        //Convert to "field" coordinates
        localRobotPos = new PVector(-localRobotPos.y, -localRobotPos.x);
        PVector rotated = localRobotPos.rotate(Math.toRadians(robotRot));

        //Add how the robot has moved to it's overall position
        setPos(PVector.add(robotPos, rotated));
        setRot(robotRot - Math.toDegrees(localRobotRot));

        //Update all of the "old" values
        oldFrontLeftEncoder = config.frontLeft.getCurrentPosition();
        oldFrontRightEncoder = config.frontRight.getCurrentPosition();
        oldBackLeftEncoder = config.backLeft.getCurrentPosition();
        oldBackRightEncoder = config.backRight.getCurrentPosition();

        if (config.getDebugMode()) config.telemetry.addLine("Pos: (" + robotPos.x + ", " + robotPos.y + ")");
        if (config.getDebugMode()) config.telemetry.addLine("Rot: " + robotRot);
    }

    public MecanumOdometer(Configurator config) {
        this.config = config;
    }

    public void beginOdometry() {
        //Update all of the "old" values
        oldFrontLeftEncoder = config.frontLeft.getCurrentPosition();
        oldFrontRightEncoder = config.frontRight.getCurrentPosition();
        oldBackLeftEncoder = config.backLeft.getCurrentPosition();
        oldBackRightEncoder = config.backRight.getCurrentPosition();

        State odometryState = new State(() -> {
            odometryLoop();
            return false;
        }, () -> {}, "Hidden");

        config.stateMachine.addState(odometryState);
    }

    public PVector getPos() {
        return robotPos;
    }

    public double getRot() {
        return robotRot;
    }

    public void setPos(PVector pos) {
        if (config.getDebugMode()) config.telemetry.addLine("Set odometry pos: (" + pos.x + ", " + pos.y + ")");
        robotPos = pos;
    }

    public void setRot(double rot) {
        if (config.getDebugMode()) config.telemetry.addLine("Set odometry rot: " + rot);
        robotRot = rot;
    }
}
