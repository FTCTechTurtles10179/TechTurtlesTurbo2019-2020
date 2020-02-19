package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

public class MecanumOdometer { //IMPORTANT!!!!! When configuring +Y is left, +X is forward! When reading values, +X is right +Y is forward!
    private Configurator config;
    private BNO055IMU imu;
    private PVector robotPos;
    private double robotRot;

    private long lastImuReadTime = 0;
    private PVector imuRobotPos;
    private double imuRobotRot;
    private double imuRotOffset;

    //How far off encoders must be from IMU to switch to the IMU
    private double bumpThreshhold = 1;

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
    double strafeEfficiency = 2.25;
    double forwardEfficiency = 1.035;

    //Configure this to our motors
    double ticksToDegrees = 0.79998;

    //K is used for math later, it is a bit off when directly calculated so it is an adjustable constant
    final double K = 35.25; //33.4988

    private void odometryLoop() {
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
        oldFrontLeftEncoder = config.frontLeft.getCurrentPosition();
        oldFrontRightEncoder = config.frontRight.getCurrentPosition();
        oldBackLeftEncoder = config.backLeft.getCurrentPosition();
        oldBackRightEncoder = config.backRight.getCurrentPosition();
    }

    public void imuLoop() { //Multiply velocity by time to get change in position, and use the IMU's compass.
        Velocity velocity = imu.getVelocity();
        double timeChange = System.currentTimeMillis() - lastImuReadTime;
        lastImuReadTime = System.currentTimeMillis();
        PVector localRobotPos = new PVector(velocity.xVeloc * timeChange, velocity.yVeloc * timeChange, velocity.zVeloc * timeChange);

        imuRobotPos = PVector.add(imuRobotPos, localRobotPos);
        imuRobotRot = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle - imuRotOffset;

        //Telemetry
        if (config.getDebugMode()) config.telemetry.addLine("IMUPos: (" + Math.round(localRobotPos.x) + ", " + Math.round(localRobotPos.y) + ")");

        if (PVector.dist(imuRobotPos, robotPos) > bumpThreshhold) {
            robotRot = imuRobotRot;
            robotPos = imuRobotPos;
        }
    }

    public MecanumOdometer(Configurator config) { //Store the config
        this.config = config;
    }

    public void beginOdometry() {
        //Get the imu
        imu = config.getIMU("imu");
        imuRotOffset = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle;

        //Update all of the "old" values
        oldFrontLeftEncoder = config.frontLeft.getCurrentPosition();
        oldFrontRightEncoder = config.frontRight.getCurrentPosition();
        oldBackLeftEncoder = config.backLeft.getCurrentPosition();
        oldBackRightEncoder = config.backRight.getCurrentPosition();

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
        imuRobotPos = pos;
    }

    public void setRot(double rot) {
        robotRot = rot;
        imuRotOffset = imuRotOffset + (imuRobotRot - rot);
    }
}
