

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp(name = "MecanumTeleOp_ProgressiveBot", group = "Linear Opmode")
public class teleop_Progressive extends LinearOpMode {

    // Motors and Servos
    private DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor, hanging1, hanging2;
    private DcMotor arm, slider;
    private Servo servo1; // 270° Servo
    private Servo grip; // Continuous Rotation Servo
    int targetPosition = 0;

    // PID Variables
    private double kP = 0.005;  // Proportional coefficient
    private double kI = 0.0;    // Integral coefficient
    private double kD = 0.0;    // Derivative coefficient
    private double wrist_folded = 0.1;
    private double wrist_spec = 1;

    private double armError = 0;
    private double armIntegral = 0;
    private double armDerivative = 0;
    private double lastError = 0;

    // Arm control variables
    private int armTargetPosition = 0;
    private static final double ARM_POWER = 0.3; // Max motor power
    private static final double DEADZONE = 0.1;

    // Slider threshold
    private static final double SLIDER_SPEED_THRESHOLD = 0.2;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize Motors
        frontLeftMotor = hardwareMap.dcMotor.get("fl");
        backLeftMotor = hardwareMap.dcMotor.get("bl");
        frontRightMotor = hardwareMap.dcMotor.get("fr");
        backRightMotor = hardwareMap.dcMotor.get("br");
        arm = hardwareMap.dcMotor.get("arm");
        slider = hardwareMap.get(DcMotor.class, "slider");
        hanging1  = hardwareMap.get(DcMotor.class, "hanging1");
        hanging2  = hardwareMap.get(DcMotor.class, "hanging2");
//        servo1 = hardwareMap.servo.get("servo1");
        grip = hardwareMap.servo.get("grip");

        // Reset and configure the arm motor encoder
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        arm.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        slider.setDirection(DcMotorSimple.Direction.REVERSE);

        slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        hanging2.setDirection(DcMotorSimple.Direction.REVERSE);

        // Wait for the game to start
        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {

            arm.setTargetPosition(armTargetPosition); //sets the target position of the arm
            if (armTargetPosition > arm.getCurrentPosition()) {//Arm is going down
                arm.setPower(1); //sets the arm power **ADJUST THIS NUMBER AS NEEDED!**
            } else if ((armTargetPosition < arm.getCurrentPosition()) && (armTargetPosition < -850)) {
                arm.setPower(1); //sets the arm power **ADJUST THIS NUMBER AS NEEDED!**
            } else {//arm is going up (front side)
                arm.setPower(1); //sets the arm power **ADJUST THIS NUMBER AS NEEDED!**
            }
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //sets the arm to brake mode to prevent sagging while raised
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION); //sets the arm to run using the built in encoder


            // Set up mecanum drive
            frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
            double y = gamepad1.left_stick_y; // Forward/backward
            double x = -gamepad1.left_stick_x; // Strafing
            double rx = -gamepad1.right_stick_x; // Turning

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y +  x - rx) / denominator;

//            if (Math.abs(x) > 0.05) {
//                frontLeftMotor.setPower(-x);
//                backLeftMotor.setPower(x);
//                frontRightMotor.setPower(-x);
//                backRightMotor.setPower(x);
//            } else {
            double powerMultiplier = 1;
            frontLeftMotor.setPower(frontLeftPower * powerMultiplier);
            backLeftMotor.setPower(backLeftPower * powerMultiplier);
            backRightMotor.setPower(backRightPower * powerMultiplier);
            frontRightMotor.setPower(frontRightPower * powerMultiplier);
//            }

            if (((gamepad2.a) && (armTargetPosition < 0))) {//manual arm down
                armTargetPosition = armTargetPosition + 10;
            } else if (((gamepad2.y) && (armTargetPosition > -1550*3))) {//manual arm up
                armTargetPosition = armTargetPosition - 10;
            }

            // Arm control with joystick
//            double armInput = -gamepad2.left_stick_y;
//            if (Math.abs(armInput) > DEADZONE) {
//                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//                arm.setPower(armInput * ARM_POWER);
//                armTargetPosition = arm.getCurrentPosition();
//            } else {
//                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//                armError = armTargetPosition - arm.getCurrentPosition();
//                armIntegral += armError;
//                armDerivative = armError - lastError;
//                double pidOutput = (kP * armError) + (kI * armIntegral) + (kD * armDerivative);
//                arm.setPower(pidOutput);
//                lastError = armError;
//            }

            // Slider control
            double sliderPower = gamepad2.left_stick_x;

            // Adjust target position based on stick input
            if (sliderPower > 0.05) {
                targetPosition += 10;  // Increment position
            } else if (sliderPower < -0.05) {
                targetPosition -= 10;  // Decrement position
            }
            else{
                targetPosition = targetPosition;
            }

            // Set the target position and switch mode
            slider.setTargetPosition(targetPosition);
            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slider.setPower(0.5);  // Adjust power as needed




//            // Servo1 control based on buttons
//            if (gamepad2.a) {
//                servo1.setPosition(wrist_spec);
//            }
//            if (gamepad2.b) {
//                servo1.setPosition(wrist_folded);
//            }

            // Passive intake
            if (gamepad2.right_bumper) {
                grip.setPosition(0.5);
            } else if (gamepad2.left_bumper) {
                grip.setPosition(0);
            }
            double hanging1p = -gamepad2.left_stick_y;
            hanging1.setPower(hanging1p*hanging1p*hanging1p);

            hanging2.setPower(-gamepad2.right_stick_y*-gamepad2.right_stick_y*-gamepad2.right_stick_y);



            // Servo2 control
//            if (gamepad2.x) {
//                servo2.setPower(1.0);  // Full speed forward
//            } else if (gamepad2.y) {
//                servo2.setPower(0.5);  // Half speed forward
//            } else {
//                servo2.setPower(0.0);  // Stop the servo when no button is pressed
//            }

            // Telemetry for debugging
            telemetry.addData("Arm Target Position", armTargetPosition);
            telemetry.addData("Arm Current Position", arm.getCurrentPosition());
            telemetry.addData("Arm Error", armError);
//            telemetry.addData("Servo1 Position", servo1.getPosition());
            telemetry.addData("Joystick Intake", gamepad2.right_stick_y);
            telemetry.addData("FL Power", frontLeftPower);
            telemetry.addData("BL Power", backLeftPower);
            telemetry.addData("FR Power", frontRightPower);
            telemetry.addData("BR Power", backRightPower);
            telemetry.update();
        }
    }
}