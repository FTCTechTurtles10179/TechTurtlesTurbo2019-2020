package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.MecanumOdometer;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@TeleOp(name="TeleOp", group="default")

public class TurtlesTeleOp extends Configurator {
    DcMotor armMotor;
    Servo claw;
    Servo claw2;
    Servo foundationGrabber;
    Servo foundationGrabber2;
    TouchSensor armToucher;
    double slowMode = 1;
    boolean slowModeJustSwapped = false;
    boolean armLimit = true;
    MecanumOdometer odometer = new MecanumOdometer(this);

    @Override
    public void setupOpMode() {
        odometer.setPos(new PVector(0, 0));

        odometer.beginOdometry();
        armMotor = getDcMotor("armMotor");
        claw = getServo("claw");
        claw2 = getServo("claw2");
        foundationGrabber = getServo("foundationGrabber");
        foundationGrabber.setPosition(1);
        foundationGrabber2 = getServo("foundationGrabber2");
        foundationGrabber2.setPosition(0);
        armToucher = getTouchSensor("armToucher");
        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        wheelController.crispDrive = true; //Make the driving crispy

        stateMachine.addState(new State(() -> { //Create a new state
            if ((gamepad1.right_bumper || gamepad1.left_bumper) && !slowModeJustSwapped) {
                slowMode = (slowMode == 1) ? 2 : 1;
            }
            slowModeJustSwapped = (gamepad1.right_bumper || gamepad1.left_bumper);

            wheelController.moveXYTurn(
                    gamepad1.right_stick_x / slowMode,
                    -gamepad1.left_stick_y / slowMode,
                    -gamepad1.left_stick_x / slowMode * 0.8
            );

            double armSpeed = -gamepad2.left_stick_y;
            if (armMotor.getCurrentPosition() <= 0 && armLimit)
                armSpeed = Range.clip(armSpeed, 0, 1);
            armMotor.setPower(Range.clip(armSpeed + 0.05, -1, 1));
            telemetry.addData("armMotor", armMotor.getCurrentPosition());

            if (gamepad1.b) wheelController.runWithoutEncoder();
            if (gamepad1.a) wheelController.runUsingEncoder();
            if (gamepad2.left_bumper) armLimit = false;
            if (gamepad2.right_bumper) armLimit = true;

            if (gamepad2.a) {
                claw.setPosition(1);
                claw2.setPosition(0);
            }
            if (gamepad2.b) {
                claw.setPosition(0);
                claw2.setPosition(1);
            }

            if (gamepad2.x) {
                foundationGrabber.setPosition(1);
                foundationGrabber2.setPosition(0);
            }
            if (gamepad2.y) {
                foundationGrabber.setPosition(0);
                foundationGrabber2.setPosition(1);
            }

            return false;
        }, () -> {}, "TeleOp")); //Don't run anything on stop, and name it TeleOp
    }
}