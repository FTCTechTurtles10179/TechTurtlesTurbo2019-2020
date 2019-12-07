package org.firstinspires.ftc.teamcode.lib.util;

import org.firstinspires.ftc.teamcode.lib.util.BooleanCommand;
import org.firstinspires.ftc.teamcode.lib.util.Command;

import static java.lang.System.currentTimeMillis;

public class State {
    private BooleanCommand program; //What this state actually runs
    private Command programOnStop; //What runs when the state is finished
    private boolean isTimer = false; //If it uses a timer or runs indefinitely
    private boolean firstRun = true; //Is it the first time the state was run?
    private long timeStarted = -1; //When the state is first run
    private long millisToRun; //If the state only needs to run for x amount of milliseconds
    private String stateName = "state"; //Cosmetic

    public State(BooleanCommand program, Command programOnStop) { //Most basic state, just a command to run indefinitely
        this.program = program;
        this.programOnStop = programOnStop;
    }

    public State(BooleanCommand program, Command programOnStop, long millisToRun) { //A state with a command that runs for a period of time
        this.program = program;
        this.programOnStop = programOnStop;
        this.millisToRun = millisToRun;
        isTimer = true;
        timeStarted = currentTimeMillis();
    }

    public State(BooleanCommand program, Command programOnStop, String stateName) { //Named version of above
        this.program = program;
        this.programOnStop = programOnStop;
        this.stateName = stateName;
    }

    public State(BooleanCommand program, Command programOnStop, long millisToRun, String stateName) { //Named version of above
        this.program = program;
        this.programOnStop = programOnStop;
        this.stateName = stateName;
        this.millisToRun = millisToRun;
        isTimer = true;
        timeStarted = currentTimeMillis();
    }

    public String getStateName() {
        return stateName;
    }

    public boolean execute() {
        if (firstRun && isTimer) {
            firstRun = false;
            timeStarted = currentTimeMillis();
        }
        boolean stop = (currentTimeMillis() >= timeStarted + millisToRun && timeStarted != -1) || program.execute(); //True if the state wants to be deactivated or time ran out
        if (stop) programOnStop.execute();
        return stop;
    }
}