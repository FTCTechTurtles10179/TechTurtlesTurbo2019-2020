package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.lib.Configurator;
import org.firstinspires.ftc.teamcode.lib.util.states.State;

@Autonomous(name="newAuto")
public class NewAuto extends Configurator {
    @Override
    public void setupOpMode() {
        stateMachine.addState(new State(() -> {
            wheelController.moveXY(-1, 0);
            return false;
        }, () -> {
            wheelController.stopWheels();
        }, 800, "moveLeft"));
    }
}
