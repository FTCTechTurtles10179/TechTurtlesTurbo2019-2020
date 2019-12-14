package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.states.State;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {
    List<State> states; //All of the current running states
    Configurator config; //Configurator instance that this StateMachine is attached to

    StateMachine(Configurator config) {
        this.config = config;
        states = new ArrayList();
    }

    public void addState(State state) { //Add a state
        states.add(state);
    }

    public void runStates() {
        List<State> statesToRemove = new ArrayList<>();
        for (State state: states) {
            if (config.debugMode && state.getStateName() != "Hidden") config.telemetry.addLine("State: " + state.getStateName());
            if (state.execute()) statesToRemove.add(state);
        }

        for (State state: statesToRemove) {
            states.remove(state);
        }
    }
}