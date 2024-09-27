package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.VisionPortal;
import android.util.Size;

@TeleOp(name = "AprilTagFollow", group = "TeleOp")
public class AprilTagFollow extends LinearOpMode {

    private AprilTagProcessor tagProcessor;
    private VisionPortal visionPortal;

    private final double DESIRED_DISTANCE = 0.5; // Desired distance from the AprilTag (meters)
    private final double kDistance = 1.0; // Proportional gain for distance control
    private final double kX = 0.01; // Proportional gain for X-axis control
    private final double kY = 0.01; // Proportional gain for Y-axis control

    private final double TARGET_X = -0.38; // Target X coordinate of the AprilTag
    private final double TARGET_Y = 22.32; // Target Y coordinate of the AprilTag
    private final double TARGET_Z = -0.15; // Target Z coordinate of the AprilTag

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    @Override
    public void runOpMode() throws InterruptedException {

        // Initialize the AprilTag processor
        tagProcessor = new AprilTagProcessor.Builder()
            .setDrawAxes(true)
            .setDrawCubeProjection(true)
            .setDrawTagID(true)
            .setDrawTagOutline(true)
            .build();

        // Initialize the Vision Portal with the webcam and the AprilTag processor
        visionPortal = new VisionPortal.Builder()
            .addProcessor(tagProcessor)
            .setCamera(hardwareMap.get(WebcamName.class, "Webcam"))
            .setCameraResolution(new Size(640, 480)) // Correct resolution type
            .build();

        // Initialize motors
        leftMotor = hardwareMap.get(DcMotor.class, "Left");
        rightMotor = hardwareMap.get(DcMotor.class, "Right");

        // Set zero power behavior
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Invert the direction of the left motor
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the start command
        waitForStart();

        while (opModeIsActive()) {
            // Default motor powers
            double leftPower = 0.0;
            double rightPower = 0.0;

            if (tagProcessor.getDetections() != null && !tagProcessor.getDetections().isEmpty()) {
                AprilTagDetection tag = tagProcessor.getDetections().get(0);

                if (tag != null && tag.ftcPose != null) {
                    double distanceError = tag.ftcPose.z - DESIRED_DISTANCE;
                    double xError = tag.ftcPose.x - TARGET_X;
                    double yError = tag.ftcPose.y - TARGET_Y;

                    // Calculate motor speeds based on errors
                    leftPower = kDistance * distanceError - kX * xError;
                    rightPower = kDistance * distanceError + kX * xError;

                    // Apply some correction for the Y-axis if needed
                    leftPower += kY * yError;
                    rightPower += kY * yError;

                    // Normalize motor powers to avoid exceeding 100%
                    double maxPower = Math.max(Math.abs(leftPower), Math.abs(rightPower));
                    if (maxPower > 1.0) {
                        leftPower /= maxPower;
                        rightPower /= maxPower;
                    }

                    // Cap the motor powers for safety
                    leftPower = Math.max(-1.0, Math.min(1.0, leftPower));
                    rightPower = Math.max(-1.0, Math.min(1.0, rightPower));
                }
            } else {
                // Control the motors based on joystick values if AprilTag is not detected
                double joystickValueRight = gamepad1.right_stick_x; // Rotate left/right
                double joystickValueLeft = -gamepad1.left_stick_y; // Move forward/backward (inverted)

                // Adjust motor powers for movement
                leftPower = joystickValueLeft + joystickValueRight;
                rightPower = joystickValueLeft - joystickValueRight;

                // Normalize motor powers to avoid exceeding 100%
                double maxPower = Math.max(Math.abs(leftPower), Math.abs(rightPower));
                if (maxPower > 1.0) {
                    leftPower /= maxPower;
                    rightPower /= maxPower;
                }
            }

            // Set motor powers
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);

            // Output tag pose data
            telemetry.addData("x", tagProcessor.getDetections().isEmpty() ? "No Tag" : tagProcessor.getDetections().get(0).ftcPose.x);
            telemetry.addData("y", tagProcessor.getDetections().isEmpty() ? "No Tag" : tagProcessor.getDetections().get(0).ftcPose.y);
            telemetry.addData("z", tagProcessor.getDetections().isEmpty() ? "No Tag" : tagProcessor.getDetections().get(0).ftcPose.z);
            telemetry.addData("roll", tagProcessor.getDetections().isEmpty() ? "No Tag" : tagProcessor.getDetections().get(0).ftcPose.roll);
            telemetry.addData("pitch", tagProcessor.getDetections().isEmpty() ? "No Tag" : tagProcessor.getDetections().get(0).ftcPose.pitch);
            telemetry.addData("yaw", tagProcessor.getDetections().isEmpty() ? "No Tag" : tagProcessor.getDetections().get(0).ftcPose.yaw);

            telemetry.update();
        }

        // Properly handle cleanup
        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);
    }
}
