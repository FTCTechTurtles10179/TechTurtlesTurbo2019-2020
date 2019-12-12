package org.firstinspires.ftc.teamcode.lib.util;

import org.firstinspires.ftc.teamcode.lib.util.command.BooleanCommand;
import org.firstinspires.ftc.teamcode.lib.util.command.Command;

import static java.lang.System.currentTimeMillis;

public class State {
    private BooleanCommand program; //What this state actually runs
    private Command programOnStop; //What runs when the state is finished
    private boolean isTimer = false; //If it uses a timer or runs indefinitely
    private boolean firstRun = true; //Is it the first time the state was run?
    private long timeStarted = -1; //When the state is first run
    private long millisToRun = 0; //If the state only needs to run for x amount of milliseconds
    private String stateName = "Unnamed"; //Cosmetic

    public State(BooleanCommand program, Command programOnStop) { //Most basic state, just a command to run indefinitely
        this.program = program;
        this.programOnStop = programOnStop;
    }

    public State(BooleanCommand program, Command programOnStop, long millisToRun) { //A state with a command that runs for a period of time
        this.program = program;
        this.programOnStop = programOnStop;
        this.millisToRun = millisToRun;
        isTimer = true;
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
    }

    public String getStateName() { //Prevent state name modification
        return stateName;
    }

    public boolean execute() {
        if (firstRun && isTimer) { //If this is a timer and it's the first time running,
            firstRun = false;
            timeStarted = currentTimeMillis(); //Then the current time is when it started
        }
        boolean stop = (!firstRun && currentTimeMillis() >= timeStarted + millisToRun) || program.execute(); //True if the state wants to be deactivated or time ran out
        if (stop) programOnStop.execute(); //Run what's on stop if we are stopping
        return stop;
    }
}