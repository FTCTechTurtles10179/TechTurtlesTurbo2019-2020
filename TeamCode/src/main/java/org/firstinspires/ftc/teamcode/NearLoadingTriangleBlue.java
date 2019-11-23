package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.Timeout;
import org.firstinspires.ftc.teamcode.lib.WheelController;
import org.firstinspires.ftc.teamcode.ConceptVuforiaNavigation;

@Autonomous(name="NearLoadingTriangleBlue")
public class NearLoadingTriangleBlue extends LinearOpMode{
    Configurator config;
    WheelController wheelController;
    VuforiaLocalizer vuforiaLocalizer;

    public void runOpMode(){
        config = new Configurator(this);
        wheelController = new WheelController(config);
        Servo foundationGrabber;
        foundationGrabber = hardwareMap.servo.get("foundationGrabber");
        foundationGrabber.setPosition(1);

        waitForStart();
        wheelController.runWithoutEncoder();

        //move to the foundation
        wheelController.moveXY(0.25, -0.25);
        Timeout.waitUnlessInterrupt(3500, () -> (!opModeIsActive()));

        //stop at the foundation
        wheelController.stopWheels();

        //deploy the foundation grabber
        foundationGrabber.setPosition(0);
        Timeout.waitUnlessInterrupt(800, () -> (!opModeIsActive()));

        //move back to the loading zone
        wheelController.moveXY(0, 0.6);
        Timeout.waitUnlessInterrupt(4300, () -> (!opModeIsActive()));

        //stop turning
        wheelController.stopWheels();

        //retract the foundation grabber
        foundationGrabber.setPosition(1);
        Timeout.waitUnlessInterrupt(800, () -> (!opModeIsActive()));

        //move forward a tad away from the wall
        wheelController.moveXY(0, -0.5);
        Timeout.waitUnlessInterrupt(100, () -> (!opModeIsActive()));

        //strafe away from the foundation
        wheelController.moveXY(-0.5, -0.2);
        Timeout.waitUnlessInterrupt(1000, () -> (!opModeIsActive()));

        //strafe into the line for a park
        wheelController.moveXY(-1, 0);
        Timeout.waitUnlessInterrupt(1000, () -> (!opModeIsActive()));

        //park
        wheelController.stopWheels();
    }
}
