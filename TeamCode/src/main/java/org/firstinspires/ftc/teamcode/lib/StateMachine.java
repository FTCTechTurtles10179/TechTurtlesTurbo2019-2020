package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.states.State;

import java.util.ArrayList;

public class StateMachine {
    ArrayList<State> states; //All of the current running states
    ArrayList<State> statesToAdd; //States that need to be added to the list of running states
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
            if (config.getDebugMode()) state.debugMode = true;
            states.add(0, state);
        }
        statesToAdd.clear();

        ArrayList<State> statesToRemove = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            if (config.getDebugMode() && states.get(i).getStateName() != "Hidden") {
                config.telemetry.addLine(
                    "State: " + states.get(i).getStateName() + "(" + states.get(i).getAvgRuntime() + "ms)"
                );
            }
            if (states.get(i).execute()) statesToRemove.add(states.get(i));
        }

        for (State state: statesToRemove) {
            states.remove(state);
        }
    }
}