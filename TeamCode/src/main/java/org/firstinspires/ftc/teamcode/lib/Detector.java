package org.firstinspires.ftc.teamcode.lib;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvPipeline;

public class Detector {
    OpMode opMode;
    OpenCvCamera webcam;
    OpenCvPipeline pipeline;

    Detector(OpMode opMode){
        this.opMode = opMode;
    }

    public void init()
    {
        int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(opMode.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        webcam.openCameraDevice();
        this.pipeline = new TestPipeline();
        webcam.setPipeline(this.pipeline);
    }
}
