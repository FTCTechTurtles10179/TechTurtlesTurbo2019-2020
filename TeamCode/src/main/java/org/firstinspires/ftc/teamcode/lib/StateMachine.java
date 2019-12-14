package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.states.State;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {
    List<State> states; //All of the current running states
    List<State> statesToAdd; //States that need to be added to the list of running states
    Configurator config; //Configurator instance that this StateMachine is attached to

    StateMachine(Configurator config) {
        this.config = config;
        statesToAdd = new ArrayList();
        states = new ArrayList();
    }

    public void addState(State state) { //Add a state safely
        statesToAdd.add(state);
    }

    void runStates() {
        for (State state: statesToAdd) {
            states.add(0, state);
        }
        statesToAdd.clear();

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