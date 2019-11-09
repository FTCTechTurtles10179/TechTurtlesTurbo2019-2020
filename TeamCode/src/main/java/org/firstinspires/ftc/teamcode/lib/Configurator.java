package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Disabled
public class Configurator {
    Telemetry telemetry;
    HardwareMap hardwareMap;

    public Configurator(OpMode opMode) {
        hardwareMap = opMode.hardwareMap;
        telemetry = opMode.telemetry;
    }

    public DcMotor getDcMotor(String name) {
        try {
            return hardwareMap.dcMotor.get(name);
        } catch (Exception e) {
            telemetry.log().add("WARNING : Could not find motor " + name + ", please add to config.");
            return hardwareMap.getAll(DcMotor.class).get(0);
        }
    }

    public Servo getServo(String name) {
        try {
            return hardwareMap.servo.get(name);
        } catch (Exception e) {
            telemetry.log().add("WARNING : Could not find servo " + name + ", please add to config.");
            return hardwareMap.getAll(Servo.class).get(0);
        }
    }

    public TouchSensor getTouchSensor(String name) {
        try {
            return hardwareMap.touchSensor.get(name);
        } catch (Exception e) {
            telemetry.log().add("WARNING : Could not find touchSensor " + name + ", please add to config.");
            return hardwareMap.getAll(TouchSensor.class).get(0);
        }
    }
}
