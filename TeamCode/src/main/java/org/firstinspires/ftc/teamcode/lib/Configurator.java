package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Hardware;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Disabled
public class Configurator {
    OpMode opMode;

    public Configurator(OpMode opMode) {
        this.opMode = opMode;
    }

    public DcMotor getDcMotor(String name) {
        try {
            return opMode.hardwareMap.dcMotor.get(name);
        } catch (Exception e) {
            opMode.telemetry.log().add("WARNING : Could not find motor " + name + ", please add to config.");
            try {
                return DcMotor.class.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public Servo getServo(String name) {
        try {
            return opMode.hardwareMap.servo.get(name);
        } catch (Exception e) {
            opMode.telemetry.log().add("WARNING : Could not find servo " + name + ", please add to config.");
            try {
                return Servo.class.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public TouchSensor getTouchSensor(String name) {
        try {
            return opMode.hardwareMap.touchSensor.get(name);
        } catch (Exception e) {
            opMode.telemetry.log().add("WARNING : Could not find touchSensor " + name + ", please add to config.");
            try {
                return TouchSensor.class.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
