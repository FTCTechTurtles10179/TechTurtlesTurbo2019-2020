package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.util.StringCommand;

public class PreInitGUI { //TODO: Update with new libraries
    String[] varNames;
    double[] setValues;
    StringCommand[] dataDisplay;
    Telemetry telemetry;
    Gamepad gamepad;
    int cursorPos;
    boolean prevUp = false;
    boolean prevDown = false;
    boolean prevInc = false;
    boolean prevDec = false;

    public PreInitGUI(String[] varNames, StringCommand[] dataDisplay, OpMode opMode) {
        this.varNames = varNames;
        this.dataDisplay = dataDisplay;
        setValues = new double[varNames.length];
        this.telemetry = opMode.telemetry;
        this.gamepad = opMode.gamepad1;
    }

    public void update() {
        telemetry.clearAll();

        telemetry.addLine("=== Telemetry Value ===");
        for (int i = 0; i < dataDisplay.length; i++) {
            telemetry.addLine(dataDisplay[i].execute());
        }

        telemetry.addLine("===   Change Data   ===");
        if (gamepad.left_stick_y < -0.5 && !prevUp && cursorPos < varNames.length-1) cursorPos++;
        if (gamepad.left_stick_y > 0.5 && !prevDown && cursorPos > 0) cursorPos--;

        for (int i = varNames.length-1; i >= 0; i--) {
            if (i == cursorPos) {
                telemetry.addData("> " + varNames[i], setValues[i]);
                if (gamepad.a && !prevInc) setValues[i]++;
                if (gamepad.b && !prevDec) setValues[i]--;
                if (gamepad.left_stick_x > 0.5) setValues[i] += 5;
                if (gamepad.left_stick_x < -0.5) setValues[i] -= 5;
                if (gamepad.x) setValues[i] = 0;
            } else {
                telemetry.addData("   " + varNames[i], setValues[i]);
            }
        }

        prevInc = gamepad.a;
        prevDec = gamepad.b;
        prevUp = (gamepad.left_stick_y > 0.5);
        prevDown = (gamepad.left_stick_y < -0.5);

        telemetry.update();
    }

    public double get(String name) {
        double value = 0;

        for (int i = 0; i < varNames.length; i++) {
            if (varNames[i] == name) {
                value = setValues[i];
            }
        }

        return value;
    }
}
