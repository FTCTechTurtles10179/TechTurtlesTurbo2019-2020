import org.firstinspires.ftc.teamcode.lib.DeadWheelOdometer;
import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.junit.Before;
import org.junit.Test;

import FakeHardware.FakeConfigurator;
import org.firstinspires.ftc.teamcode.lib.DeadWheelOdometer.DeadWheelEncoderValues;
import org.firstinspires.ftc.teamcode.lib.DeadWheelOdometer.Position2D;
import TestUtilities.SimFormat;

public class DeadWheelOdometerTest {


    DeadWheelOdometer deadWheelOdometer;
    FakeConfigurator fakeConfigurator;
    private boolean isHeadingPrinted;

    @Before
    public void setupTest() {
        fakeConfigurator = new FakeConfigurator();
        fakeConfigurator.init();
        deadWheelOdometer = new DeadWheelOdometer(fakeConfigurator);
        deadWheelOdometer.setPos(new PVector(0,0));
        deadWheelOdometer.beginOdometry(); // Essential for zeroing out the old encoder positions.
        fakeConfigurator.setDebugMode(false);
        this.isHeadingPrinted = false;
    }

    @Test
    public void initialize_deadWheels() {
        deadWheelOdometer.setPos(new PVector(12,34));
        System.out.println( deadWheelOdometer.getPos().toString() );
        deadWheelOdometer.beginOdometry();
    }

    @Test
    public void run_isolated_odometry_calculation() {
        System.out.println("Encoder Raw Input test");
        System.out.println( deadWheelOdometer.getPos().toString() );

        // Basic input/output
        Position2D position2D;
        DeadWheelEncoderValues deadWheelEncoderValues = new DeadWheelEncoderValues(0,0,0);
        position2D = deadWheelOdometer.odometryCalculation(deadWheelEncoderValues);
        System.out.println(position2D);

        // Same as above, but streamlined.
        setEncoderAndDisplayPosition(0,0,0); // Why are we getting movement?
        setEncoderAndDisplayPosition(0,0,0);
        setEncoderAndDisplayPosition(100,0,100);
        setEncoderAndDisplayPosition(-100,0,-100);
        setEncoderAndDisplayPosition(100,0,-100);
        setEncoderAndDisplayPosition(-100,0,100);
    }



//    @Test
    public void run_state_machine() {
        deadWheelOdometer.setPos(new PVector(56,78));
        System.out.println( deadWheelOdometer.getPos().toString() );

        fakeConfigurator.setDebugMode(false); // If true, triggers a Null Pointer Error on controllers.

        for(int i =0; i<10; ++i) {
            fakeConfigurator.stateMachine.runStates();
        }
    }

    private  void setEncoderAndDisplayPosition(double left, double center, double right) {
        setEncoderAndDisplayPosition(new DeadWheelEncoderValues(left,center,right));
    }

    private void setEncoderAndDisplayPosition(DeadWheelEncoderValues deadWheelEncoderValues) {
        Position2D position2D;
        position2D = deadWheelOdometer.odometryCalculation(deadWheelEncoderValues);


        // Print Output
        if(!isHeadingPrinted) {
            System.out.println("");
            System.out.print(SimFormat.padCenteredStringTo(35, "Encoder Ticks"));
            System.out.print("|");
            System.out.println(SimFormat.padCenteredStringTo(38, "Position Absolute"));

            // Second line
            System.out.print(SimFormat.padCenteredStringTo(35, "left      center      right"));
            System.out.print("|");
            System.out.println(SimFormat.padCenteredStringTo(38, "X          Y         Theta"));

            System.out.println("-------------------------------------------------------------------------------------");

            isHeadingPrinted = true;
        }

        System.out.print(SimFormat.padStringTo(35, deadWheelEncoderValues.toString()));
        System.out.print("|");
        System.out.println(SimFormat.padCenteredStringTo(38, position2D.toString()));

    }
}
