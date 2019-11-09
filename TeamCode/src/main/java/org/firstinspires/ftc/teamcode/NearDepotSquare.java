package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.lib.AutoController;
import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.Timeout;
import org.firstinspires.ftc.teamcode.lib.WheelController;
import org.firstinspires.ftc.teamcode.lib.util.BooleanCommand;

@Autonomous(name="NearDepotSquare")
public class NearDepotSquare extends LinearOpMode {
    Configurator config;
    WheelController wheelController;

    public void runOpMode(){
        config = new Configurator(this);
        wheelController = new WheelController(config);
        Servo foundationGrabber;
        foundationGrabber = hardwareMap.servo.get("foundationGrabber");
        foundationGrabber.setPosition(1);
        waitForStart();

        //moves to the skybridge
        wheelController.moveXY(0,0.5);
        Timeout.waitUnlessInterrupt(1400, () -> (!opModeIsActive()));

        //park
        wheelController.stopWheels();
    }
}
