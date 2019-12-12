package org.firstinspires.ftc.teamcode.lib;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.lib.util.pipelines.TestPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvPipeline;

public class Detector {
    Configurator configurator;
    OpenCvCamera webcam;
    OpenCvPipeline pipeline;

    Detector(Configurator configurator){
        this.configurator = configurator;
    }

    public void init()
    {
        int cameraMonitorViewId = configurator.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", configurator.hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(configurator.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        webcam.openCameraDevice();
        this.pipeline = new TestPipeline();
        webcam.setPipeline(this.pipeline);
    }
}
