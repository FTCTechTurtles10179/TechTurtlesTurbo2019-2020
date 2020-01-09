package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

public class MecanumOdometer {
    private Configurator config;
    private Position robotPos;
    private Orientation robotRot;


    public MecanumOdometer(Configurator config, Position startingPos, Orientation statringRot) {
        this.config = config;
        setPos(startingPos);
        setRot(statringRot);

        State debugState = State.blank();
        State odometryState = new State(() -> {

            return false;
        }, () -> {}, "Odometry");

        config.stateMachine.addState(odometryState);
        if (config.getDebugMode()) config.stateMachine.addState(debugState);
    }

    public Position getPos() {
        return robotPos;
    }

    public Orientation getRot() {
        return robotRot;
    }

    void setPos(Position pos) {
        robotPos = pos;
    }

    void setRot(Orientation rot) {
        robotRot = rot;
    }
}
