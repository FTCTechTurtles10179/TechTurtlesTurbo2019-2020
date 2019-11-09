package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutoController;
import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.WheelController;

@Autonomous(name="red1")
public class red1 extends LinearOpMode {
    Configurator config;
    WheelController wheelController;

    public void runOpMode(){
        config = new Configurator(this);
        wheelController = new WheelController(config);
        Servo foundationGrabber;
        foundationGrabber = hardwareMap.servo.get("foundationGrabber");
        waitForStart();

        //moves sideways to accommodate for other bot
        wheelController.moveXY(0.5,0);
        sleep(500);
        wheelController.stopWheels();

        //moves backwards to the foundation
        wheelController.moveXY(0,-0.5);
        sleep(3000);
        wheelController.stopWheels();

        foundationGrabber.setPosition(0);

        wheelController.moveXY(0,0.5);
        sleep(300);
        wheelController.stopWheels();

        foundationGrabber.setPosition(1);

        wheelController.moveXY(0,0.5);
        sleep(900);
        wheelController.stopWheels();
    }
}
