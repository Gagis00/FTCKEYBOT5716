package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "MovementComplete", group = "TeleOp")
public class MovementComplete extends OpMode {
    
    TouchSensor LimitS;

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor hexMotor;
    private Servo SLeft;
    private Servo SRight;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        // Initialize the motors and servos from the hardware map
        try {
            
            LimitS = hardwareMap.get(TouchSensor.class, "LimitS");
            
            leftMotor = hardwareMap.get(DcMotor.class, "Left");
            rightMotor = hardwareMap.get(DcMotor.class, "Right");

            // Set motors to zero power behavior (e.g., brake or float)
            leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            
            // Initialize hex motor
            hexMotor = hardwareMap.get(DcMotor.class, "HexMotor");
            hexMotor.setDirection(DcMotor.Direction.FORWARD);
            hexMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            
            // Initialize servos
            SLeft = hardwareMap.get(Servo.class, "SLeft");
            SRight = hardwareMap.get(Servo.class, "SRight");

            telemetry.addData("Status", "Initialized");
        } catch (Exception e) {
            telemetry.addData("Error", "Hardware component not found");
        }
    }

    @Override
    public void loop() {
        
        if (LimitS.isPressed()) {
                hexMotor.setPower(0.4);
            } else { // Otherwise, run the motor
            
            // Control hex motor with buttons
            if (gamepad1.x) {
                hexMotor.setPower(0.5); // Move motor up
            } else if (gamepad1.a) {
                hexMotor.setPower(-0.3); // Move motor down
            } else {
                hexMotor.setPower(0.0); // Stop motor
            }
            
            }
        
        
        // Check if the motors and servos are initialized before using them
        if (leftMotor != null && rightMotor != null) {
            // Get the y-axis value of the left joystick
            double joystickValueRight = gamepad1.right_stick_x; // No inversion
            double joystickValueLeft = gamepad1.left_stick_y; // No inversion

            // Set the motor power based on the joystick value
            leftMotor.setPower(joystickValueRight);
            rightMotor.setPower(joystickValueRight);
            leftMotor.setPower(joystickValueLeft);
            rightMotor.setPower(-joystickValueLeft);
            

            // Control servos with L2 and R2
            if (gamepad1.left_trigger > 0.5) {
                SLeft.setPosition(1.0);  // Move SLeft to position 1.0
                SRight.setPosition(0.0); // Move SRight to position 0.0
            } else if (gamepad1.right_trigger > 0.5) {
                SLeft.setPosition(0.0);  // Move SLeft to position 0.0
                SRight.setPosition(1.0); // Move SRight to position 1.0
            } else {
                SLeft.setPosition(0.5);  // Neutral position for SLeft
                SRight.setPosition(0.5); // Neutral position for SRight
            }

            // Add telemetry data
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

        // Update the telemetry
        telemetry.update();
    }

    @Override
    public void stop() {
        // Ensure the motors and servos are stopped when the OpMode is stopped
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
