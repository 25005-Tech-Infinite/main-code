import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import pedroPathing.constants.FConstants;
import pedroPathing.constants.LConstants;

@TeleOp(name = "MecanumTeleOp_TechInfinity_challenge", group = "Linear Opmode")
public class teleope_challenge extends LinearOpMode {

    // Motors and Servos
    private DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;
    private DcMotor arm, slider;
    private Servo servo1; // 270° Servo
    private Servo servo2; // Continuous Rotation Servo

    // PID Variables
    private double kP = 0.005;  // Proportional coefficient
    private double kI = 0.0;    // Integral coefficient
    private double kD = 0.0;    // Derivative coefficient
    private double wrist_folded = 0.1;
    private double wrist_spec = 1;

    private double multiplier = 1;

    private double armError = 0;
    private double armIntegral = 0;
    private double armDerivative = 0;
    private double lastError = 0;

    // Arm control variables
    private int armTargetPosition = 0;

    private int sliderPosition = 0;
    private static final double ARM_POWER = 0.3; // Max motor power
    private static final double DEADZONE = 0.1;

    private int currentPosition = 0;

    // Slider threshold
    private static final double SLIDER_SPEED_THRESHOLD = 0.2;

    private Follower follower;
    private final Pose startPose = new Pose(0,0,0);

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize Motors
        frontLeftMotor = hardwareMap.dcMotor.get("fl_drive");
        backLeftMotor = hardwareMap.dcMotor.get("bl_drive");
        frontRightMotor = hardwareMap.dcMotor.get("fr_drive");
        backRightMotor = hardwareMap.dcMotor.get("br_drive");
        arm = hardwareMap.dcMotor.get("arm");
        slider = hardwareMap.get(DcMotor.class, "slider");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");

        // Reset and configure the arm motor encoder
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setDirection(DcMotorSimple.Direction.REVERSE);

//        slider.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        slider.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        currentPosition = slider.getCurrentPosition();

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start
        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {

            arm.setTargetPosition(armTargetPosition); //sets the target position of the arm
            if (armTargetPosition > arm.getCurrentPosition()) {//Arm is going down
                arm.setPower(.15); //sets the arm power **ADJUST THIS NUMBER AS NEEDED!**
            } else if ((armTargetPosition < arm.getCurrentPosition()) && (armTargetPosition < -1000)) {
                arm.setPower(.25); //sets the arm power **ADJUST THIS NUMBER AS NEEDED!**
            } else {//arm is going up (front side)
                arm.setPower(0.25); //sets the arm power **ADJUST THIS NUMBER AS NEEDED!**
            }
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //sets the arm to brake mode to prevent sagging while raised
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION); //sets the arm to run using the built in encoder

//            slider.setTargetPosition(sliderPosition);
//            slider.setPower(0.5);
//            slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            // Set up mecanum drive
            frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotor.Direction.REVERSE);
            double y = -gamepad1.left_stick_y; // Forward/backward
            double x = gamepad1.left_stick_x; // Strafing
            double rx = gamepad1.right_stick_x; // Turning

            if(gamepad1.right_bumper){
                multiplier = 1;
            }
            if(gamepad1.left_bumper){
                multiplier = 0.5;
            }

//            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
//            double frontLeftPower = (y + x + rx) / denominator;
//            double backLeftPower = (y - x + rx) / denominator;
//            double frontRightPower = (y - x - rx) / denominator;
//            double backRightPower = (y +  x - rx) / denominator;
//
//
//
//            frontLeftMotor.setPower(frontLeftPower * multiplier);
//            backLeftMotor.setPower(backLeftPower * multiplier);
//            backRightMotor.setPower(backRightPower * multiplier);
//            frontRightMotor.setPower(frontRightPower * multiplier);

            if (((gamepad2.right_stick_y > 0.05) && (armTargetPosition < 0))) {//manual arm down
                armTargetPosition = armTargetPosition + 1;
                arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            } else if (((gamepad2.right_stick_y < 0.05) && (armTargetPosition > -1550))) {//manual arm up
                armTargetPosition = armTargetPosition - 1;
                arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            }
            else {
                arm.setPower(0);
            }

            if (gamepad2.dpad_up) armTargetPosition = -1114;
            if (gamepad2.dpad_down) armTargetPosition = 0;


            arm.setTargetPosition(armTargetPosition);

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

//            if (Math.abs(gamepad2.left_stick_x) > 0.5) {
//                if (gamepad2.left_stick_x > 0) {
//                    if (armTargetPosition > -1100+200) {
//                        if (sliderPosition < 2200) {
//                            sliderPosition = sliderPosition + 15;
//                        } else {
//                            sliderPosition = sliderPosition;
//                        }
//                    } else {
//                        sliderPosition = sliderPosition + 15;
//                    }
//                } else if ((sliderPosition - 15) > 0) {
//                    sliderPosition = sliderPosition - 15;
//                } else {
//                    sliderPosition = 0;
//                }
//            }

            if (armTargetPosition > -1100+200) {
                if ((getCurrentSliderPosition() < 2200 && -gamepad2.left_stick_y > 0) || -gamepad2.left_stick_y < 0) {
                    double sliderPower = -gamepad2.left_stick_y;
                    slider.setPower(sliderPower);
                } else {
                    slider.setPower(0);
                }
            } else {
                double sliderPower = -gamepad2.left_stick_y;
                slider.setPower(sliderPower);
            }

//            if (Math.abs(gamepad2.left_stick_x) > 0.5) {
//                if (gamepad2.left_stick_x > 0) {
//                    if (armTargetPosition > -1100+200) {
//                        if (sliderPosition < 2200) {
//                            sliderPosition = sliderPosition + 15;
//                        } else {
//                            sliderPosition = sliderPosition;
//                        }
//                    } else {
//                        sliderPosition = sliderPosition + 15;
//                    }
//                } else if ((sliderPosition - 15) > 0) {
//                    sliderPosition = sliderPosition - 15;
//                } else {
//                    sliderPosition = 0;
//                }
//            }



//            // Servo1 control based on buttons
//            if (gamepad2.a) {
//                servo1.setPosition(wrist_spec);
//            }
//            if (gamepad2.b) {
//                servo1.setPosition(wrist_folded);
//            }

            // Active intake
            if (gamepad2.left_bumper) {
                servo1.setPosition(0.03);
            } else if (gamepad2.right_bumper) {
                servo1.setPosition(0.2);
            }
//            if (gamepad2.left_bumper) {
//                servo2.setPower(0.5);
//            } else if (gamepad2.right_bumper) {
//                servo2.setPower(-0.5);
//            } else {
//                servo2.setPower(-0.0);
//            }

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

            telemetry.addData("Slider Target Position", sliderPosition);
            telemetry.addData("Slider Current Position", getCurrentSliderPosition());

            telemetry.addData("Arm Error", armError);
            telemetry.addData("Servo1 Position", servo1.getPosition());
            telemetry.addData("Joystick Intake", gamepad2.right_stick_y);

            telemetry.update();
        }
        // -1124 high arm
        // slider 2205
    }

    private int getCurrentSliderPosition() {
        return slider.getCurrentPosition() - currentPosition;
    }
}