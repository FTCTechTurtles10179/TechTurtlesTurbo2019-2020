package org.firstinspires.ftc.teamcode.lib.util.pipelines;

import org.firstinspires.ftc.teamcode.lib.util.processingLibs.PVector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SkystoneFinder extends OpenCvPipeline {
    private PVector[] bounds = new PVector[4];
    public double[] listOfThings;

    @Override
    public Mat processFrame(Mat input) {
        Mat yCbCrChan2Mat = new Mat();
        Mat outputMat = new Mat();
        Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);//converts rgb to ycrcb
        Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);//takes cb difference and stores

        for () {

        }

        return outputMat;
    }
}
