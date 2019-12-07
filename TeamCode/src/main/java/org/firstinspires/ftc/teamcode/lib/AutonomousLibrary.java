package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.State;

public class AutonomousLibrary extends Configurator {
    double cmToClickForward = 16.5;
    double cmToClickRight = 16.5;

    public void moveForwardCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.avgEncoder();
        stateMachine.addState(new State(() -> {
            if (Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) >= 5 * cmToClickForward) {
                wheelController.moveXY(0, speed);
            } else {
                wheelController.moveXY(0, speed/3);
            }
            return !(Math.abs((wheelController.avgEncoder() - startingEncoder) - (distance * cmToClickForward)) <= 0.1 * cmToClickForward);
        }, () -> {}, "autoLibMoveForwardCm"));
    }

    public void strafeRightCentimeters(double distance, double speed) {
        double startingEncoder = wheelController.avgEncoder();
        stateMachine.addState(new State(() -> {
            if (Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) >= 5 * cmToClickRight) {
                wheelController.moveXY(speed, 0);
            } else {
                wheelController.moveXY(speed/3, 0);
            }
            return !(Math.abs((wheelController.rightEncoder() - startingEncoder) - (distance * cmToClickRight)) <= 0.1 * cmToClickRight);
        }, () -> {}, "autoLibMoveRightCm"));
    }
}
