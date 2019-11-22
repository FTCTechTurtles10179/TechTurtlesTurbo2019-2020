package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.lib.PreInitGUI;
import org.firstinspires.ftc.teamcode.lib.util.StringCommand;

@TeleOp(name="Pushbot", group="default")

public class GoodPushbot extends OpMode {
    DcMotor left;
    DcMotor right;
    DcMotor arm;

    Servo lclaw;
    Servo rclaw;

    PreInitGUI gui;

    @Override
    public void init() {
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        arm = hardwareMap.dcMotor.get("arm");
        lclaw = hardwareMap.servo.get("lclaw");
        rclaw = hardwareMap.servo.get("rclaw");

        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        left.setDirection(DcMotor.Direction.REVERSE);

        gui = new PreInitGUI(new String[]{"armMin", "armMax"}, new StringCommand[]{() -> ("Arm position: " + arm.getCurrentPosition())}, this);
    }

    @Override
    public void init_loop() {
        gui.update();
    }

    @Override
    public void start() {
        telemetry.clearAll();
        telemetry.update();
    }

    @Override
    public void loop() {
        double turn = gamepad1.left_stick_x;
        double power = gamepad1.left_stick_y;
        double leftPower = Range.clip(power - turn, -1, 1);
        double rightPower = Range.clip(power + turn, -1, 1);

        left.setPower(leftPower);
        right.setPower(rightPower);


        if (arm.getCurrentPosition() < gui.get("armMin")) {
            arm.setPower(0.1);
        } else if (arm.getCurrentPosition() > gui.get("armMax")) {
            arm.setPower(-0.1);
        } else {
            arm.setPower(gamepad1.right_stick_y);
        }

        if (gamepad1.a) {
            lclaw.setPosition(0);
            rclaw.setPosition(1);
        }
        if (gamepad1.b) {
            lclaw.setPosition(1);
            rclaw.setPosition(0);
        }
    }
}
