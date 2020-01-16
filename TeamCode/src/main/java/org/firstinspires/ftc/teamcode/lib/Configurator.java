package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.hardware.bosch.BNO055IMU;
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

    private boolean debugMode = false;

    public StateMachine stateMachine;
    public WheelController wheelController;

    public BNO055IMU getIMU(String name) {
        try {
            return hardwareMap.get(BNO055IMU.class, name);
        } catch (Exception e) {
            telemetry.addLine("WARNING : Could not find IMU \"" + name + "\", please add to config.");
            try {
                BNO055IMU imu = hardwareMap.get(BNO055IMU.class, name);
                BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

                parameters.mode = BNO055IMU.SensorMode.IMU;
                parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
                parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
                parameters.loggingEnabled = false;

                imu.initialize(parameters);

                return BNO055IMU.class.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public DcMotor getDcMotor(String name) {
        try {
            return hardwareMap.dcMotor.get(name);
        } catch (Exception e) {
            telemetry.addLine("WARNING : Could not find motor \"" + name + "\", please add to config.");
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
            telemetry.addLine("WARNING : Could not find servo \"" + name + "\", please add to config.");
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
            telemetry.addLine("WARNING : Could not find touchSensor \"" + name + "\", please add to config.");
            try {
                return TouchSensor.class.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    @Override
    public void init() { //On init, setup motors, stateMachine, and wheelController
        frontLeft = getDcMotor("frontLeft");
        frontRight = getDcMotor("frontRight");
        backLeft = getDcMotor("backLeft");
        backRight = getDcMotor("backRight");

        stateMachine = new StateMachine(this);
        wheelController = new WheelController(this);

        setupOpMode();
    }

    @Override
    public void init_loop() {
        if (gamepad1.a && gamepad1.b && gamepad1.x && gamepad1.y) debugMode = true;
    }

    public abstract void setupOpMode();

    @Override
    public void loop() {
        telemetry.clearAll();
        if (gamepad1.a && gamepad1.b && gamepad1.x && gamepad1.y) debugMode = true;
        wheelController.detectMotorFault();
        stateMachine.runStates();
        telemetry.update();
    }

    public boolean getDebugMode() {
        return debugMode;
    }
}
