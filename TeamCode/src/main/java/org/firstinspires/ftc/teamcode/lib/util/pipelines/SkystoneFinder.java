package org.firstinspires.ftc.teamcode.lib.util.pipelines;

import org.firstinspires.ftc.teamcode.lib.util.processingLibs.PVector;
import org.opencv.core.Mat;
import org.openftc.easyopencv.OpenCvPipeline;

public class SkystoneFinder extends OpenCvPipeline {
    private PVector[] bounds = new PVector[4];

    @Override
    public Mat processFrame(Mat input) {
        input.get(0, 0);


        return null;
    }
}
