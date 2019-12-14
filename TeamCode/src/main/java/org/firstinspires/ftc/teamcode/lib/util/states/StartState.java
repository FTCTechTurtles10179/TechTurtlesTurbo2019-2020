package org.firstinspires.ftc.teamcode.lib.util.states;

import org.firstinspires.ftc.teamcode.lib.util.command.BooleanCommand;
import org.firstinspires.ftc.teamcode.lib.util.command.Command;

import static java.lang.System.currentTimeMillis;

public class StartState extends State {
    private Command programOnStart; //What runs when the state is started

    public StartState(Command programOnStart, BooleanCommand program, Command programOnStop) { //Most basic state, just a command to run indefinitely
        super(program, programOnStop);
        this.programOnStart = programOnStart;
    }

    public StartState(Command programOnStart, BooleanCommand program, Command programOnStop, long millisToRun) { //A state with a command that runs for a period of time
        super(program, programOnStop, millisToRun);
        this.programOnStart = programOnStart;
    }

    public StartState(Command programOnStart, BooleanCommand program, Command programOnStop, String stateName) { //Named version of above
        super(program, programOnStop, stateName);
        this.programOnStart = programOnStart;
    }

    public StartState(Command programOnStart, BooleanCommand program, Command programOnStop, long millisToRun, String stateName) { //Named version of above
        super(program, programOnStop, millisToRun, stateName);
        this.programOnStart = programOnStart;
    }

    public static StartState blank() {
        return new StartState(() -> {}, () -> {return true;}, () -> {}, "Hidden");
    }

    public String getStateName() { //Prevent state name modification
        return stateName;
    }

    @Override
    public boolean execute() {
        if (firstRun) { //If this is the first time running
            programOnStart.execute();
            if (isTimer) { //If it is also a timer
                firstRun = false;
                timeStarted = currentTimeMillis(); //Then the current time is when it started
            }
        }
        boolean stop = (!firstRun && currentTimeMillis() >= timeStarted + millisToRun) || program.execute(); //True if the state wants to be deactivated or time ran out
        if (stop) programOnStop.execute(); //Run what's on stop if we are stopping
        return stop;
    }
}