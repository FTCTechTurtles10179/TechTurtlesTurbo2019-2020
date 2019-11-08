package org.firstinspires.ftc.teamcode;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutoController;
import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.WheelController;

import static java.lang.Thread.sleep;

@Autonomous(name="red2")
public class red2 extends LinearOpMode {
    Configurator config;
    WheelController wheelController;

    public void runOpMode(){
        config = new Configurator(this);
        wheelController = new WheelController(config);
        Servo foundationGrabber;
        foundationGrabber = hardwareMap.servo.get("foundationGrabber");
        waitForStart();
        wheelController.runWithoutEncoder();

        //moves sideways to accommodate for other bot
        wheelController.moveXY(-0.5,0);
        sleep(500);
        wheelController.stopWheels();

        //moves forward under the skybridge
        wheelController.moveXY(0,-0.5);
        sleep(1250);
        wheelController.stopWheels();
    }
}
