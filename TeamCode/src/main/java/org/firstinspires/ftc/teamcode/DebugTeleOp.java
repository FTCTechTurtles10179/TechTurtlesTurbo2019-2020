package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.WheelController;

@TeleOp(name="Debug", group="default")

public class DebugTeleOp extends OpMode {
    Configurator config;
    WheelController wheelController;

    @Override
    public void init() {
        config = new Configurator(this);
        wheelController = new WheelController(config);
    }

        @Override
    public void loop() {
        wheelController.moveXY(0, gamepad1.left_stick_y);
        telemetry.addData("overall encoder", (wheelController.leftEncoder() + wheelController.rightEncoder())/2);
    }
}
