package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.teamcode.lib.util.states.State;

import java.util.ArrayList;
import java.util.List;

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
            states.add(0, state);
        }
        statesToAdd.clear();

        ArrayList<State> statesToRemove = new ArrayList<>();
        State[] statesAsArray = states.toArray(new State[0]);
        for (int i = 0; i < statesAsArray.length; i++) {
            if (config.getDebugMode() && statesAsArray[i].getStateName() != "Hidden") {
                config.telemetry.addLine("State: " + statesAsArray[i].getStateName());
            }
            if (statesAsArray[i].execute()) statesToRemove.add(statesAsArray[i]);
        }

        for (State state: statesToRemove) {
            states.remove(state);
        }
    }
}