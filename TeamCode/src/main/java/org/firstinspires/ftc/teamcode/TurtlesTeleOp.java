package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.WheelController;

@TeleOp(name="TeleOp", group="default")

public class TurtlesTeleOp extends OpMode {
    Configurator config;
    WheelController wheelController;
    DcMotor armMotor;
    Servo claw;
    Servo foundationGrabber;

    @Override
    public void init() {
        config = new Configurator(this);
        wheelController = new WheelController(config);
        armMotor = config.getDcMotor("armMotor");
        claw = config.getServo("claw");
        foundationGrabber = config.getServo("foundationGrabber");
    }

    @Override
    public void loop() {
        wheelController.moveXYTurn(wheelController.sixthPowerOf(gamepad1.right_stick_x), wheelController.sixthPowerOf(gamepad1.left_stick_y), -wheelController.sixthPowerOf(gamepad1.left_stick_x));
        armMotor.setPower(Range.clip(-wheelController.sixthPowerOf(gamepad2.right_stick_y) + 0.05, -1, 1));

        if (gamepad1.b) wheelController.runWithoutEncoder();
        if (gamepad1.a) wheelController.runUsingEncoder();
        if (gamepad1.x) {
            wheelController.moveXYTurn((wheelController.sixthPowerOf(gamepad1.right_stick_x)/2), (wheelController.sixthPowerOf(gamepad1.left_stick_y)/2), (-wheelController.sixthPowerOf(gamepad1.left_stick_x)/2));
        }
        if (gamepad1.y){
            wheelController.moveXYTurn(wheelController.sixthPowerOf(gamepad1.right_stick_x), wheelController.sixthPowerOf(gamepad1.left_stick_y), -wheelController.sixthPowerOf(gamepad1.left_stick_x));
        }

        if (gamepad2.a) claw.setPosition(0);
        if (gamepad2.b) claw.setPosition(1);
        if (gamepad2.x) foundationGrabber.setPosition(1);
        if (gamepad2.y) foundationGrabber.setPosition(0);

        telemetry.addData("frontLeftEncoder", wheelController.frontLeft.getCurrentPosition());
        telemetry.addData("frontRightEncoder", wheelController.frontRight.getCurrentPosition());
        telemetry.addData("backLeftEncoder", wheelController.backLeft.getCurrentPosition());
        telemetry.addData("backRightEncoder", wheelController.backRight.getCurrentPosition());
        telemetry.update();
    }
}