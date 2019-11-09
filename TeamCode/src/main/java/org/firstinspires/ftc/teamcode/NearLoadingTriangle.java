package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutoController;
import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.Timeout;
import org.firstinspires.ftc.teamcode.lib.WheelController;

import static java.lang.Thread.sleep;

@Autonomous(name="NearLoadingTriangle")
public class NearLoadingTriangle extends LinearOpMode {
    Configurator config;
    WheelController wheelController;

    public void runOpMode(){
        config = new Configurator(this);
        wheelController = new WheelController(config);
        Servo foundationGrabber;
        foundationGrabber = hardwareMap.servo.get("foundationGrabber");
        waitForStart();
        wheelController.runWithoutEncoder();

        //move to the foundation
        wheelController.moveXY(0, -0.5);
        Timeout.waitUnlessInterrupt(800, () -> {return opModeIsActive();});

        //stop at the foundation
        wheelController.stopWheels();

        //deploy the foundation grabber
        foundationGrabber.setPosition(1);

        //move back to the loading zone
        wheelController.moveXY(0, 0.5);
        Timeout.waitUnlessInterrupt(1100, () -> {return opModeIsActive();});

        //park!
        wheelController.stopWheels();
    }
}
