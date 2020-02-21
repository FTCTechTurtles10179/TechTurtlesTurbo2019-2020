package FakeHardware;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.lib.Configurator;
import FakeHardware.FakeDcMotorLib.FakeDcMotor;


import java.util.HashMap;


public class FakeConfigurator extends Configurator {

    Telemetry telemetry = new FakeTelemetryLib.FakeTelemetry();





    @Override
    public void setupOpMode() {

    }

    // Handle providing fake dc motors.
    HashMap<String, FakeDcMotor> motorHashMap = new HashMap<>();
    @Override
    public DcMotor getDcMotor(String name) {

        FakeDcMotor motor = motorHashMap.get(name);
        if(motor == null) {
            motor = new FakeDcMotor(name);
            motorHashMap.put(name,motor);
        }
        return (DcMotor) motor;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

//    config.telemetry.addLine('caption','content')

}
