package FakeHardware;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

// Does not extend DcMotor
// Need to return a MotorConfigurationType object
public class FakeDcMotorLib {

    static public class FakeDcMotor extends FakeDcMotorLib.FakeDcMotorPhysics implements DcMotor {

        public FakeDcMotor() {
            this.motorName = "defaultMotorName";
        }

        public FakeDcMotor(String name) {
            this.motorName = motorName;
        }

        @Override
        public MotorConfigurationType getMotorType() {
            return null;
        }

        @Override
        public void setMotorType(MotorConfigurationType motorType) {

        }

        @Override
        public DcMotorController getController() {
            return null;
        }

        @Override
        public int getPortNumber() {
            return 0;
        }

        @Override
        public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {

        }

        @Override
        public ZeroPowerBehavior getZeroPowerBehavior() {
            return null;
        }

        @Override
        public void setPowerFloat() {

        }

        @Override
        public boolean getPowerFloat() {
            return false;
        }

        @Override
        public void setTargetPosition(int position) {

        }

        @Override
        public int getTargetPosition() {
            return 0;
        }

        @Override
        public boolean isBusy() {
            return false;
        }

        @Override
        public int getCurrentPosition() {
            return 0;
        }

        @Override
        public void setMode(RunMode mode) {

        }

        @Override
        public RunMode getMode() {
            return null;
        }

        @Override
        public void setDirection(Direction direction) {

        }

        @Override
        public Direction getDirection() {
            return null;
        }

        @Override
        public Manufacturer getManufacturer() {
            return null;
        }

        @Override
        public String getDeviceName() {
            return null;
        }

        @Override
        public String getConnectionInfo() {
            return null;
        }

        @Override
        public int getVersion() {
            return 0;
        }

        @Override
        public void resetDeviceConfigurationForOpMode() {

        }

        @Override
        public void close() {

        }
    }

    static class FakeDcMotorPhysics {


        double power = 0.0;
        double ticks = 0;
        private double time = 0.0;
        public String motorName;

        public FakeDcMotorPhysics() {
            this.motorName = "defaultMotorName";
        }

        FakeDcMotorPhysics(String name) {
            this.motorName = motorName;
        }


        // Fake Physics - basic acceleration limit
        private double maxTicksPerSecond = 2300; // Telemetry suggests velocity of 2500 ticks/second
        private double maxTicksPerSecondPerSecond = 6000; // Telemetry suggests acceleration of 9000 ticks/s/s
        private double currentTicksPerSecond = 0;


        public void updateAndIntegratePosition(double time) {
            double deltaTime = time - this.time;
            if (deltaTime == 0.0) return;

            // Limit Acceleration
            double desiredTicksPerSecond = power * maxTicksPerSecond;
            double desiredAcceleration_TicksPerSecPerSec = (desiredTicksPerSecond - currentTicksPerSecond) / deltaTime;
            if (Math.abs(desiredAcceleration_TicksPerSecPerSec) > maxTicksPerSecondPerSecond) {
                double accelerationSign = desiredAcceleration_TicksPerSecPerSec / Math.abs(desiredAcceleration_TicksPerSecPerSec);
                // Limit the acceleration
                currentTicksPerSecond += accelerationSign * maxTicksPerSecondPerSecond * deltaTime;
//                System.out.println("ACCELERATION LIMIT ACTIVATED");
            } else {
                currentTicksPerSecond = desiredTicksPerSecond;
            }

            // Limit Velocity
            if (Math.abs(currentTicksPerSecond) > maxTicksPerSecond) {
                currentTicksPerSecond *= Math.abs(maxTicksPerSecond / currentTicksPerSecond);
            }


            this.ticks += currentTicksPerSecond * deltaTime;
            this.time = time;
        }

        public void setPower(double power) {
            this.power = power;
        }

        public double getPower() {
            return power;
        }

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }

        public double getTicks() {
            return ticks;
        }
    }

}
