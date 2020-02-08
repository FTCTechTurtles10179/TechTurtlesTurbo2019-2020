package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.lib.util.states.State;

public class WheelController { //This class can be changed for each drive train without breaking our library
    //The configurator that this WheelController is attached to
    Configurator config;

    //Variables for fault detection
    private int oldFrontLeftEncoder = 0;
    private int oldFrontRightEncoder = 0;
    private int oldBackLeftEncoder = 0;
    private int oldBackRightEncoder = 0;
    private boolean faultOccured = false;

    public boolean crispDrive = false; //CrispDrive - Uses encoders to ensure stops
    private double crispDriveAdjustSpeed = 0.5;

    //Define the four wheels since we are using a mecanum drive train
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public int avgEncoder() { //Returns the average of both wheel encoders, or how
        return (frontLeft.getCurrentPosition() + frontRight.getCurrentPosition())/2;
    }

    public int leftEncoder() { //Returns the frontLeft encoder
        return frontLeft.getCurrentPosition();
    }

    public int rightEncoder() { //Returns the frontright encoder
        return frontRight.getCurrentPosition();
    }

    public void resetLeftEncoder() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void resetRightEncoder() {
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void moveXY(double tx, double ty) { //Take in translateX and translateY
        //Start with how fast we are moving forward
        double frontLeftSpd = -ty;
        double frontRightSpd = -ty;
        double backLeftSpd = -ty;
        double backRightSpd = -ty;

        //Add the strafing values
        frontLeftSpd -= tx;
        backLeftSpd += tx;
        frontRightSpd += tx;
        backRightSpd -= tx;

        //Make sure the final output is safe for the motors, or a speed from -1 to 1
        frontLeft.setPower(Range.clip(frontLeftSpd, -1, 1));
        frontRight.setPower(Range.clip(frontRightSpd, -1, 1));
        backLeft.setPower(Range.clip(backLeftSpd, -1, 1));
        backRight.setPower(Range.clip(backRightSpd, -1, 1));
    }

    public void moveTurn(double tspeed) {  //Take in turnSpeed
        //Start with the turn values
        double frontLeftSpd = tspeed;
        double backLeftSpd = tspeed;
        double frontRightSpd = -tspeed;
        double backRightSpd = -tspeed;

        //Make sure the final output is safe for the motors, or a speed from -1 to 1
        frontLeft.setPower(Range.clip(frontLeftSpd, -1, 1));
        frontRight.setPower(Range.clip(frontRightSpd, -1, 1));
        backLeft.setPower(Range.clip(backLeftSpd, -1, 1));
        backRight.setPower(Range.clip(backRightSpd, -1, 1));
    }

    public void moveXYTurn(double tx, double ty, double tspeed) {
        //Start with how fast we are moving forward
        double frontLeftSpd = -ty;
        double frontRightSpd = -ty;
        double backLeftSpd = -ty;
        double backRightSpd = -ty;

        //Add the strafing values
        frontLeftSpd -= tx;
        backLeftSpd += tx;
        frontRightSpd += tx;
        backRightSpd -= tx;

        //Add the turning values
        frontLeftSpd += tspeed;
        backLeftSpd += tspeed;
        frontRightSpd -= tspeed;
        backRightSpd -= tspeed;

        //Make sure the final output is safe for the motors, or a speed from -1 to 1
        frontLeft.setPower(Range.clip(frontLeftSpd, -1, 1));
        frontRight.setPower(Range.clip(frontRightSpd, -1, 1));
        backLeft.setPower(Range.clip(backLeftSpd, -1, 1));
        backRight.setPower(Range.clip(backRightSpd, -1, 1));
    }

    public void stopWheels() {
        moveXY(0,0);
    }

    public void runUsingEncoder() {
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void runWithoutEncoder() {
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void detectMotorFault() {
        if (faultOccured && !config.stateMachine.paused) config.telemetry.addLine("A motor fault occurred!");

        //Check if the front left motor is powered but the encoder is not moving
        if (Math.abs(frontLeft.getPower()) > 0.6 && oldFrontLeftEncoder == frontLeft.getCurrentPosition()) {
            //If so, there is an encoder issue so we disable encoders
            faultOccured = true;
            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //Do the same for the other motors
        if (Math.abs(frontRight.getPower()) > 0.6 && oldFrontRightEncoder == frontRight.getCurrentPosition()) {
            faultOccured = true;
            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (Math.abs(backLeft.getPower()) > 0.6 && oldBackLeftEncoder == backLeft.getCurrentPosition()) {
            faultOccured = true;
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (Math.abs(backRight.getPower()) > 0.6 && oldBackRightEncoder == backRight.getCurrentPosition()) {
            faultOccured = true;
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (crispDrive) crispDriveAdjust();

        //Update all of the "old" values
        oldFrontLeftEncoder = frontLeft.getCurrentPosition();
        oldFrontRightEncoder = frontRight.getCurrentPosition();
        oldBackLeftEncoder = backLeft.getCurrentPosition();
        oldBackRightEncoder = backRight.getCurrentPosition();
    }

    private void crispDriveAdjust() {
        if (!faultOccured) { //Dont run this code if something is wrong with the encoders
            //Check if the front left motor is stopped but still moving
            if (Math.abs(frontLeft.getPower()) < 0.01 && oldFrontLeftEncoder != frontLeft.getCurrentPosition()) {
                //If so, the robot is drivting. Reverse power!
                if (frontLeft.getCurrentPosition() < oldFrontLeftEncoder) {
                    frontLeft.setPower(crispDriveAdjustSpeed);
                } else {
                    frontLeft.setPower(-crispDriveAdjustSpeed);
                }
            }
            //Do the same for the other motors
            if (Math.abs(frontRight.getPower()) < 0.01 && oldFrontRightEncoder != frontRight.getCurrentPosition()) {
                //If so, the robot is drivting. Reverse power!
                if (frontRight.getCurrentPosition() < oldFrontRightEncoder) {
                    frontRight.setPower(crispDriveAdjustSpeed);
                } else {
                    frontRight.setPower(-crispDriveAdjustSpeed);
                }
            }
            if (Math.abs(backLeft.getPower()) < 0.01 && oldBackLeftEncoder != backLeft.getCurrentPosition()) {
                //If so, the robot is drivting. Reverse power!
                if (backLeft.getCurrentPosition() < oldBackLeftEncoder) {
                    backLeft.setPower(crispDriveAdjustSpeed);
                } else {
                    backLeft.setPower(-crispDriveAdjustSpeed);
                }
            }
            if (Math.abs(backRight.getPower()) < 0.01 && oldBackRightEncoder != backRight.getCurrentPosition()) {
                //If so, the robot is drivting. Reverse power!
                if (backRight.getCurrentPosition() < oldBackRightEncoder) {
                    backRight.setPower(crispDriveAdjustSpeed);
                } else {
                    backRight.setPower(-crispDriveAdjustSpeed);
                }
            }
        }
    }

    public WheelController(Configurator config) {
        //Save the configurator for later.
        this.config = config;

        //Setup all the motors to what config says they are
        frontLeft = config.frontLeft;
        frontRight = config.frontRight;
        backLeft = config.backLeft;
        backRight = config.backRight;

        //Debug state
        config.stateMachine.addState(new State(() -> {
            if (config.getDebugMode()) { //If debug mode is active, telemetry the encoders of all wheels
                config.telemetry.addData("frontLeftEncoder", frontLeft.getCurrentPosition());
                config.telemetry.addData("frontRightEncoder", frontRight.getCurrentPosition());
                config.telemetry.addData("backLeftEncoder", backLeft.getCurrentPosition());
                config.telemetry.addData("backRightEncoder", backRight.getCurrentPosition());
            }
            return false;
        }, () -> {}, "Hidden"));

        runUsingEncoder(); //Turn on encoder speed adjustments

        //Reverse frontLeft and backLeft so the wheels all go forward
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}