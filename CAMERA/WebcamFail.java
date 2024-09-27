package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp(name = "WebcamFail", group = "TeleOp")
public class WebcamFail extends OpMode {

    TouchSensor LimitS;
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor hexMotor;
    private Servo SLeft;
    private Servo SRight;

    private ElapsedTime runtime = new ElapsedTime();

    OpenCvWebcam webcam;

    @Override
    public void init() {
        try {
            LimitS = hardwareMap.get(TouchSensor.class, "LimitS");

            leftMotor = hardwareMap.get(DcMotor.class, "Left");
            rightMotor = hardwareMap.get(DcMotor.class, "Right");

            leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            hexMotor = hardwareMap.get(DcMotor.class, "HexMotor");
            hexMotor.setDirection(DcMotor.Direction.FORWARD);
            hexMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            SLeft = hardwareMap.get(Servo.class, "SLeft");
            SRight = hardwareMap.get(Servo.class, "SRight");

            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

            webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam"), cameraMonitorViewId);

            webcam.setPipeline(new ObjectDetectionPipeline());

            webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                @Override
                public void onOpened() {
                    telemetry.addData("Camera Status", "Opened and Streaming");
                    telemetry.update();
                    webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                }

                @Override
                public void onError(int errorCode) {
                    telemetry.addData("Camera Error", errorCode);
                    telemetry.update();
                }
            });

            telemetry.addData("Status", "Initialized");
        } catch (Exception e) {
            telemetry.addData("Error", "Hardware component not found");
        }
    }

    @Override
    public void loop() {
        if (LimitS.isPressed()) {
            hexMotor.setPower(0.4);
        } else {
            if (gamepad1.x) {
                hexMotor.setPower(0.5);
            } else if (gamepad1.a) {
                hexMotor.setPower(-0.3);
            } else {
                hexMotor.setPower(0.0);
            }
        }

        if (leftMotor != null && rightMotor != null) {
            double joystickValueRight = gamepad1.right_stick_x;
            double joystickValueLeft = gamepad1.left_stick_y;

            leftMotor.setPower(joystickValueRight);
            rightMotor.setPower(joystickValueRight);
            leftMotor.setPower(joystickValueLeft);
            rightMotor.setPower(-joystickValueLeft);

            if (gamepad1.left_trigger > 0.5) {
                SLeft.setPosition(1.0);
                SRight.setPosition(0.0);
            } else if (gamepad1.right_trigger > 0.5) {
                SLeft.setPosition(0.0);
                SRight.setPosition(1.0);
            } else {
                SLeft.setPosition(0.5);
                SRight.setPosition(0.5);
            }

            telemetry.addData("Joystick Value Right", joystickValueRight);
            telemetry.addData("Joystick Value Left", joystickValueLeft);
            telemetry.addData("Left Motor Power", leftMotor.getPower());
            telemetry.addData("Right Motor Power", rightMotor.getPower());
            telemetry.addData("Hex Motor Power", hexMotor.getPower());
            telemetry.addData("SLeft Position", SLeft.getPosition());
            telemetry.addData("SRight Position", SRight.getPosition());
            telemetry.addData("Runtime", runtime.toString());
        } else {
            telemetry.addData("Status", "Motor(s) or Servo(s) not initialized");
        }

        telemetry.update();
    }

    @Override
    public void stop() {
        if (leftMotor != null) {
            leftMotor.setPower(0.0);
        }
        if (rightMotor != null) {
            rightMotor.setPower(0.0);
        }
        if (hexMotor != null) {
            hexMotor.setPower(0.0);
        }
        if (SLeft != null) {
            SLeft.setPosition(0.5);
        }
        if (SRight != null) {
            SRight.setPosition(0.5);
        }
    }
}
