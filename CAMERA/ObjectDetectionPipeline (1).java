package org.firstinspires.ftc.teamcode;

import org.openftc.easyopencv.OpenCvPipeline;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.Core;

import java.util.ArrayList;
import java.util.List;

public class ObjectDetectionPipeline extends OpenCvPipeline {

    private Mat yCbCr = new Mat();
    private Mat mask = new Mat();
    private List<MatOfPoint> contours = new ArrayList<>();

    @Override
    public Mat processFrame(Mat input) {
        // Convert the input frame to YCrCb color space
        Imgproc.cvtColor(input, yCbCr, Imgproc.COLOR_RGB2YCrCb);

        // Create a binary mask for detecting objects of a certain color (example: detect red)
        Scalar lowerBound = new Scalar(0, 133, 77); // Example values for color range
        Scalar upperBound = new Scalar(255, 173, 127); // Example values for color range
        Core.inRange(yCbCr, lowerBound, upperBound, mask);

        // Find contours in the mask
        contours.clear();
        Imgproc.findContours(mask, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Draw contours on the original image
        for (MatOfPoint contour : contours) {
            Imgproc.drawContours(input, contours, -1, new Scalar(0, 255, 0), 2);
        }

        return input;
    }
}
