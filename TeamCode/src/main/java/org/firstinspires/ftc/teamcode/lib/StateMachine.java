package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.State;

import java.util.ArrayList;

public class StateMachine {
    ArrayList<State> states; //All of the current running states
    Configurator config; //Configurator instance that this StateMachine is attached to
    public boolean debugMode = false;

    StateMachine(Configurator config) {
        this.config = config;
        states = new ArrayList<>();
    }

    public void addState(State state) {
        states.add(state);
    }

    public void runStates() {
        for (State state: states) {
            if (debugMode) config.telemetry.addLine("State: " + state.getStateName());
            if (!state.execute()) states.remove(state);
        }
    }
}