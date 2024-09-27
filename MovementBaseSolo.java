package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

//nombre de el código y el modo
@TeleOp(name = "MovementBaseSolo", group = "TeleOp")
public class MovementBaseSolo extends OpMode {

    //ambos motores
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    
    //tiempo activo
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        // inicia motores
        try {
            leftMotor = hardwareMap.get(DcMotor.class, "Left");
            rightMotor = hardwareMap.get(DcMotor.class, "Right");

            //comportamiento
            leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //información mostrada
            telemetry.addData("Status", "Initialized");
        } catch (Exception e) {
            telemetry.addData("Error", "Motor 'Left' or 'Right' not found");
        }
    }

    @Override
    public void loop() {
        // checa si los motores existen
        if (leftMotor != null && rightMotor != null) {
            // valor x de el joystick derecho
            double joystickValueRight = gamepad1.right_stick_x; // No inversion
            
            // valor 7 de el joystick izquierdo
            double joystickValueLeft = gamepad1.left_stick_y; // No inversion

            // los motores se mueven de acuerdo al joystick (derecha)
            leftMotor.setPower(joystickValueRight);
            rightMotor.setPower(joystickValueRight);
            
            // los motores se mueven de acuerdo al joystick (izquierdo)
            leftMotor.setPower(joystickValueLeft);
            rightMotor.setPower(-joystickValueLeft);

            // información mostrada
            telemetry.addData("Joystick Value", joystickValueRight);
            telemetry.addData("Left Motor Power", leftMotor.getPower());
            telemetry.addData("Right Motor Power", rightMotor.getPower());
            
            telemetry.addData("Runtime", runtime.toString());
        } else {
            telemetry.addData("Status", "Motor(s) not initialized");
        }

        // se recarga al inicio
        telemetry.update();
    }

    @Override
    public void stop() {
        // motores detenidos al final
        if (leftMotor != null) {
            leftMotor.setPower(0.0);
        }
        if (rightMotor != null) {
            rightMotor.setPower(0.0);
        }
    }
}
