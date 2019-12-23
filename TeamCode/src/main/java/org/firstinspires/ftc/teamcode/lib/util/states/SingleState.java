package org.firstinspires.ftc.teamcode.lib.util.states;

import org.firstinspires.ftc.teamcode.lib.util.command.BooleanCommand;
import org.firstinspires.ftc.teamcode.lib.util.command.Command;

import static java.lang.System.currentTimeMillis;

public class SingleState extends State {
    public SingleState(Command program) { //Most basic state, just a command to run once
        super(() -> true, program);
    }

    public SingleState(Command program, String stateName) { //Named version of above
        super(() -> true, program, stateName);
    }

    public static SingleState blank() {
        return new SingleState(() -> {}, "Hidden");
    }
}