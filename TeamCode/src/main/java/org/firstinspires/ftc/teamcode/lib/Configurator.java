package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Disabled
public abstract class Configurator extends OpMode{
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public boolean debugMode = false;

    private int oldFrontLeftEncoder = 999999;
    private int oldFrontRightEncoder = 999999;
    private int oldBackLeftEncoder = 999999;
    private int oldBackRightEncoder = 999999;

    public StateMachine stateMachine;
    public WheelController wheelController;

    public DcMotor getDcMotor(String name) {
        try {
            return hardwareMap.dcMotor.get(name);
        } catch (Exception e) {
            telemetry.addLine("WARNING : Could not find motor " + name + ", please add to config.");
            try {
                return DcMotor.class.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public Servo getServo(String name) {
        try {
            return hardwareMap.servo.get(name);
        } catch (Exception e) {
            telemetry.addLine("WARNING : Could not find servo " + name + ", please add to config.");
            try {
                return Servo.class.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public TouchSensor getTouchSensor(String name) {
        try {
            return hardwareMap.touchSensor.get(name);
        } catch (Exception e) {
            telemetry.addLine("WARNING : Could not find touchSensor " + name + ", please add to config.");
            try {
                return TouchSensor.class.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    public void init() {
        frontLeft = getDcMotor("frontLeft");
        frontRight = getDcMotor("frontRight");
        backLeft = getDcMotor("backLeft");
        backRight = getDcMotor("backRight");

        stateMachine = new StateMachine(this);
        wheelController = new WheelController(this);

        setupOpMode();
    }

    public abstract void setupOpMode();

    @Override
    public void loop() {
        detectMotorFault();
        stateMachine.runStates();
    }


    public void detectMotorFault() {
        //Check if the front left motor is powered but the encoder is not moving
        if (frontLeft.getPower() != 0 && oldFrontLeftEncoder == frontLeft.getCurrentPosition()) {
            //If so, there is an encoder issue so we disable encoders
            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        //Do the same for the other motors
        if (frontRight.getPower() != 0 && oldFrontRightEncoder == frontRight.getCurrentPosition()) {
            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (backLeft.getPower() != 0 && oldBackLeftEncoder == backLeft.getCurrentPosition()) {
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (backRight.getPower() != 0 && oldBackRightEncoder == backRight.getCurrentPosition()) {
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public boolean getMotorsMoving() {
        return (oldFrontLeftEncoder != frontLeft.getCurrentPosition() || oldFrontRightEncoder != frontRight.getCurrentPosition() || oldBackLeftEncoder != backLeft.getCurrentPosition() || oldBackRightEncoder != backRight.getCurrentPosition());
    }
}
