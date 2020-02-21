import org.firstinspires.ftc.teamcode.lib.DeadWheelOdometer;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.junit.Before;
import org.junit.Test;

import FakeHardware.FakeConfigurator;

public class DeadWheelOdometerTest {


    DeadWheelOdometer deadWheelOdometer;
    FakeConfigurator fakeConfigurator;

    @Before
    public void setupTest() {
        fakeConfigurator = new FakeConfigurator();
        fakeConfigurator.init();
        deadWheelOdometer = new DeadWheelOdometer(fakeConfigurator);

    }

    @Test
    public void initialize_deadWheels() {
        deadWheelOdometer.setPos(new PVector(12,34));
        System.out.println( deadWheelOdometer.getPos().toString() );
        deadWheelOdometer.beginOdometry();




    }
}
