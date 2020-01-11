package org.firstinspires.ftc.teamcode.lib.util.pipelines;

import org.firstinspires.ftc.teamcode.lib.util.data.PVector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class SkystoneFinder extends OpenCvPipeline {
    private PVector[] bounds = new PVector[4];
    double[] colors = {0, 0, 0, 0};
    double threshhold = 0;

    @Override
    public Mat processFrame(Mat input) {
        Mat yCbCrChan2Mat = new Mat();
        Mat outputMat;
        Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);//converts rgb to ycrcb
        Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);//takes cb difference and stores
        double[][] farnessAverage = new double[yCbCrChan2Mat.rows()][yCbCrChan2Mat.cols()];

        for (int x = 2; x < yCbCrChan2Mat.rows()-2; x++) {
            for (int y = 2; y < yCbCrChan2Mat.cols()-2; y++) {
                double shadeOfCr = yCbCrChan2Mat.get(x, y)[0];
                double farnessTotal = 0;

                for (int i = 0; i < colors.length; i++) {
                    farnessTotal += Math.abs(shadeOfCr - colors[i]);
                }

                farnessAverage[x][y] = farnessTotal/colors.length;
            }
        }

        Point topLeftCorner = null;
        Point bottomRightCorner = null;

        for (int x = 2; x < farnessAverage.length-2; x++) {
            for (int y = 2; y < farnessAverage[0].length - 2; y++) {
                if (farnessAverage[x][y] <= threshhold) {
                    if (topLeftCorner == null) {
                        topLeftCorner = new Point(x, y);
                    } else {
                        bottomRightCorner = new Point(x, y);
                    }
                }
            }
        }

        if (topLeftCorner == null || bottomRightCorner == null || topLeftCorner.x == 2 || topLeftCorner.y == 2 || bottomRightCorner.x == 475 || topLeftCorner.y == 635) {
            System.out.println("No bounding box found for threshhold " + threshhold);
            outputMat = processFrame(input, (threshhold + 5) % 255);
        } else {
            System.out.println("Bounding box was found for threshhold " + threshhold);
            Rect rect = new Rect((int)topLeftCorner.x, (int)topLeftCorner.y, (int)(bottomRightCorner.x - topLeftCorner.x), (int)(bottomRightCorner.y - topLeftCorner.y));
            System.out.println(rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height);
            outputMat = yCbCrChan2Mat.submat(rect);
        }

        return outputMat;
    }

    public Mat processFrame(Mat input, double threshhold) {
        Mat yCbCrChan2Mat = new Mat();
        Mat outputMat;
        Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);//converts rgb to ycrcb
        Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);//takes cb difference and stores
        double[][] farnessAverage = new double[yCbCrChan2Mat.rows()][yCbCrChan2Mat.cols()];

        for (int x = 2; x < yCbCrChan2Mat.rows()-2; x++) {
            for (int y = 2; y < yCbCrChan2Mat.cols()-2; y++) {
                double shadeOfCr = yCbCrChan2Mat.get(x, y)[0];
                double farnessTotal = 0;

                for (int i = 0; i < colors.length; i++) {
                    farnessTotal += Math.abs(shadeOfCr - colors[i]);
                }

                farnessAverage[x][y] = farnessTotal/colors.length;
            }
        }

        Point topLeftCorner = null;
        Point bottomRightCorner = null;

        for (int x = 2; x < farnessAverage.length-2; x++) {
            for (int y = 2; y < farnessAverage[0].length - 2; y++) {
                if (farnessAverage[x][y] <= threshhold) {
                    if (topLeftCorner == null) {
                        topLeftCorner = new Point(x, y);
                    } else {
                        bottomRightCorner = new Point(x, y);
                    }
                }
            }
        }

        if (topLeftCorner == null || bottomRightCorner == null || topLeftCorner.x == 2 || topLeftCorner.y == 2 || bottomRightCorner.x == 475 || topLeftCorner.y == 635) {
            System.out.println("No bounding box found for threshhold " + threshhold);
            outputMat = processFrame(input, (threshhold + 5) % 255);
        } else {
            System.out.println("Bounding box was found for threshhold " + threshhold);
            Rect rect = new Rect((int)topLeftCorner.x, (int)topLeftCorner.y, (int)(bottomRightCorner.x - topLeftCorner.x), (int)(bottomRightCorner.y - topLeftCorner.y));
            System.out.println(rect.x + ", " + rect.y + ", " + rect.width + ", " + rect.height);
            outputMat = yCbCrChan2Mat.submat(rect);
        }

        return outputMat;
    }
}
