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
    private double stopDist = 10;
    private double turnSpeed = 0.1;
    private double speed = 0.3;

    ArrayList<PVector> targetPos = new ArrayList<>();
    ArrayList<Double> targetRot = new ArrayList<>();
    ArrayList<State> states = new ArrayList<>();
    DeadWheelOdometer odometry = new DeadWheelOdometer(this);

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
//        PVector fieldMotion = PVector.sub(targetPos.get(0), odometry.getPos());
//        double moveSpeed = fieldMotion.mag() > slowDist ? speed : (fieldMotion.mag()/slowDist) * speed;
//        PVector botMotion = fieldMotion.normalize().rotate(Math.toRadians(odometry.getRot())).mult(moveSpeed);
//
//        double fieldTurn = odometry.getRot() - targetRot.get(0);
//        double turnSpeed = Math.abs(fieldTurn) > slowDist ? speed : (fieldTurn/slowDist) * speed;
//        double botTurn = Range.clip(fieldTurn, -turnSpeed, turnSpeed);
        if (states.size() > 0 && targetPos.size() > 0 && targetRot.size() >0) {
            PVector fieldMotion = PVector.sub(targetPos.get(0), odometry.getPos());
            double botSpeed = Range.clip(fieldMotion.mag() / (slowDist * speed), -speed, speed);
            PVector botMotion = fieldMotion.setMag(botSpeed).rotate(Math.toRadians(-odometry.getRot()));

            double botTurn;
            if (leastDist(odometry.getRot(), targetRot.get(0)) < stopDist) {
                botTurn = 0;
            } else if (leastDirection(odometry.getRot(), targetRot.get(0))) {
                botTurn = turnSpeed;
            } else {
                botTurn = -turnSpeed;
            }

            wheelController.moveXYTurn(botMotion.x, botMotion.y, botTurn);

            if (PVector.dist(odometry.getPos(), targetPos.get(0)) < stopDist && leastDist(odometry.getRot(), targetRot.get(0)) < stopDist) {
                if (states.get(0) != null) stateMachine.addState(states.get(0));
                states.remove(0);
                targetPos.remove(0);
                targetRot.remove(0);
            }

            if (getDebugMode()) {
                telemetry.addLine("Target X:" + targetPos.get(0).x + " Y:" + targetPos.get(0).y + " R:" + targetRot.get(0));
            }
        }
    }

    public void setTargetXYRot(PVector pos, double rot) {
        targetPos.add(pos);
        targetRot.add(rot);
        states.add(null);
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
