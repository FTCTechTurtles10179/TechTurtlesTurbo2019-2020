package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

import java.util.ArrayList;

public abstract class AutonomousLibrary extends Configurator {
    private double cmToClickForward = 16.5;
    private double cmToClickRight = 24.3;
    private double slowDown = 3;
    private double slowDist = 15;
    private double stopDist = 1;
    private double turnSpeed = 0.1;
    private double speed = 0.3;

    ArrayList<PVector> targetPos = new ArrayList<>();
    ArrayList<Double> targetRot = new ArrayList<>();
    ArrayList<State> states = new ArrayList<>();
    MecanumOdometer odometry = new MecanumOdometer(this);

    public void initializeOdometry(PVector startingPos, double startingRot) {
        odometry.setPos(startingPos);
        odometry.setRot(startingRot);
        odometry.beginOdometry();

        stateMachine.addState(new State(() -> {
            updateWheelSpeed();
            return false;
        }, () -> {}, "AutoLibUpdateWheels"));
    }

    private void updateWheelSpeed() {
        PVector botMotion = new PVector(0,0);

        if (odometry.getPos().x < targetPos.get(0).x) {
            botMotion.x = 1;
        } else {
            botMotion.x = -1;
        }

        if (odometry.getPos().y < targetPos.get(0).y) {
            botMotion.y = 1;
        } else {
            botMotion.y = -1;
        }

        wheelController.moveXYTurn(botMotion.x, botMotion.y, 0);

        if (PVector.dist(odometry.getPos(), targetPos.get(0)) < stopDist/* && leastDist(odometry.getRot(), targetRot.get(0)) < stopDist*/) {
            if (states.get(0).getStateName() != "Hidden") stateMachine.addState(states.get(0));
            states.remove(0);
            targetPos.remove(0);
            targetRot.remove(0);
        }

        if (getDebugMode()) telemetry.addLine("Target: (" + targetPos.get(0).x + ", " + targetPos.get(0).y + ") " + targetRot.get(0) + "°");
    }

    public void setTargetXYRot(PVector pos, double rot) {
        targetPos.add(pos);
        targetRot.add(rot);
        states.add(State.blank());
    }

    public void setTargetXYRot(PVector pos, double rot, State onFinished) {
        targetPos.add(pos);
        targetRot.add(rot);
        states.add(onFinished);
    }

    private boolean leastDirection(double degFrom, double degTo) { // false is left true is right
        if (degFrom == degTo) return true;

        double directDistance = Math.abs(degFrom - degTo);
        double aroundDistance;

        if (degFrom > degTo) {
            aroundDistance = degTo + (360 - degFrom);

            if (aroundDistance > directDistance) {
                return false;
            } else {
                return true;
            }
        } else {
            aroundDistance = degFrom + (360 - degTo);

            if (aroundDistance > directDistance) {
                return true;
            } else {
                return false;
            }
        }
    }

    private double leastDist(double degFrom, double degTo) {
        if (degFrom == degTo) return 0;

        double directDistance = Math.abs(degFrom - degTo);
        double aroundDistance;

        if (degFrom > degTo) {
            aroundDistance = degTo + (360 - degFrom);
        } else {
            aroundDistance = degFrom + (360 - degTo);
        }

        if (aroundDistance > directDistance) {
            return directDistance;
        } else {
            return aroundDistance;
        }
    }
}
