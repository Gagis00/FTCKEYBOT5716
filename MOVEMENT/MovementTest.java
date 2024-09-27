package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "MovementTest", group = "TeleOp")
public class MovementTest extends OpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    
    private DcMotor hexMotor;
    
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        // Initialize the motors from the hardware map
        try {
            leftMotor = hardwareMap.get(DcMotor.class, "Left");
            rightMotor = hardwareMap.get(DcMotor.class, "Right");

            // Set motors to zero power behavior (e.g., brake or float)
            leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            
                //hex motor
            
                hexMotor = hardwareMap.get(DcMotor.class, "HexMotor");

                // Set motor direction (assuming "up" direction is positive power)
                hexMotor.setDirection(DcMotor.Direction.FORWARD);

                // Set motor to zero power behavior (e.g., brake or float)
                hexMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            

            telemetry.addData("Status", "Initialized");
        } catch (Exception e) {
            telemetry.addData("Error", "Motor 'Left' or 'Right' not found");
        }
    }

    @Override
    public void loop() {
        // Check if the motors are initialized before using them
        if (leftMotor != null && rightMotor != null) {
            // Get the y-axis value of the left joystick
            double joystickValueRight = gamepad1.right_stick_x; // No inversion
            
            double joystickValueLeft = gamepad1.left_stick_y; // No inversion

            // Set the motor power based on the joystick value
            leftMotor.setPower(joystickValueRight);
            rightMotor.setPower(joystickValueRight);
            
            leftMotor.setPower(joystickValueLeft);
            rightMotor.setPower(-joystickValueLeft);
            
            //Hex motor
            if (gamepad1.x) {
            // "X" button pressed: move motor up
            hexMotor.setPower(0.3); // Full power up
        } else if (gamepad1.a) {
            // "A" button pressed: move motor down
            hexMotor.setPower(-0.3); // Full power down (reverse direction)
        } else {
            // No button pressed: stop the motor
            hexMotor.setPower(0.0);
        }

            // Add telemetry data
            telemetry.addData("Joystick Value", joystickValueRight);
            telemetry.addData("Left Motor Power", leftMotor.getPower());
            telemetry.addData("Right Motor Power", rightMotor.getPower());
            
            telemetry.addData("Motor Power", hexMotor.getPower());
            telemetry.addData("Runtime", runtime.toString());
        } else {
            telemetry.addData("Status", "Motor(s) not initialized");
        }

        // Update the telemetry
        telemetry.update();
    }

    @Override
    public void stop() {
        // Ensure the motors are stopped when the OpMode is stopped
        if (leftMotor != null) {
            leftMotor.setPower(0.0);
        }
        if (rightMotor != null) {
            rightMotor.setPower(0.0);
        }
        
        hexMotor.setPower(0.0);
    }
}
