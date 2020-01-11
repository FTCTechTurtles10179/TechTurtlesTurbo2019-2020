package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

public class MecanumOdometer { //IMPORTANT!!!!! When configuring +Y is left, +X is forward! When reading values, +X is right +Y is forward!
    private Configurator config;
    private PVector robotPos;
    private double robotRot;

    //Utility to find how much encoders have changed
    private int oldFrontLeftEncoder = 0;
    private int oldFrontRightEncoder = 0;
    private int oldBackLeftEncoder = 0;
    private int oldBackRightEncoder = 0;

    //Configure these to our wheels and wheelbase, measure in centimeters
    private PVector frontLeftWheelPos = new PVector(14.5, 19.0);
    private PVector backLeftWheelPos = new PVector(-14.5, 19.0);
    private PVector frontRightWheelPos = new PVector(14.5, -19.0);
    private PVector backRightWheelPos = new PVector(-14.5, -19.0);
    double wheelRadius = 4.75;

    //Configure this to our motors
    int ticksToDegrees = 28/360;

    //K is used for math later
    double K = Math.abs(frontLeftWheelPos.x) + Math.abs(frontLeftWheelPos.y);

    private void odometryLoop() {
        /* The math behind this code: (W for omega)
        If W_n is the rotation change for wheel n, r is the wheel radius, and K is |X of wheel n| + |Y of wheel n|,
        then we can find the robot's velocity V and rotation velocity W_v using
        
        r * (1/4W_1 + 1/4W_2 + 1/4W_3 + 1/4W_4)                =   V_x
        r * (-1/4W_1 + 1/4W_2 - 1/4W_3 + 1/4W_4)               =   V_y
        r * (-1/(4K)W_1 - 1/(4K)W_2 + 1/(4K)W_3 + 1/(4K)W_4)   =   W_v
        */

        int frontLeftDegreeChange = (oldFrontLeftEncoder - config.frontLeft.getCurrentPosition()) * ticksToDegrees;
        int frontRightDegreeChange = (oldFrontRightEncoder - config.frontRight.getCurrentPosition()) * ticksToDegrees;
        int backLeftDegreeChange = (oldBackLeftEncoder - config.backLeft.getCurrentPosition()) * ticksToDegrees;
        int backRightDegreeChange = (oldBackRightEncoder - config.backRight.getCurrentPosition()) * ticksToDegrees;

        PVector localRobotPos = new PVector(
                wheelRadius * (0.25 * (frontLeftDegreeChange + frontRightDegreeChange + backLeftDegreeChange + backRightDegreeChange)),
                wheelRadius * ((0.25 * (frontRightDegreeChange + backLeftDegreeChange)) - (0.25 * (frontLeftDegreeChange + backLeftDegreeChange)))
        );

        double localRobotRot = wheelRadius * ((1/(4 * K) * (backRightDegreeChange + frontRightDegreeChange)) - (1/(4 * K) * (backLeftDegreeChange + frontLeftDegreeChange)));

        //Convert to "field" coordinates
        localRobotPos.rotate(robotRot);
        localRobotPos = new PVector(-localRobotPos.y, localRobotPos.x);

        //Ad how the robot has moved to it's overall position
        PVector.add(robotPos, localRobotPos);
        robotRot += localRobotRot;

        //Update all of the "old" values
        oldFrontLeftEncoder = config.frontLeft.getCurrentPosition();
        oldFrontRightEncoder = config.frontRight.getCurrentPosition();
        oldBackLeftEncoder = config.backLeft.getCurrentPosition();
        oldBackRightEncoder = config.backRight.getCurrentPosition();
    }

    public MecanumOdometer(Configurator config) {
        this.config = config;
    }

    public void beginOdometry() {
        State debugState = State.blank();
        State odometryState = new State(() -> {
            odometryLoop();
            return false;
        }, () -> {}, "Hidden");

        config.stateMachine.addState(odometryState);
        if (config.getDebugMode()) config.stateMachine.addState(debugState);
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
