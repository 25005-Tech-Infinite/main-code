
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "SimpleAuto", group = "Drive")
public class SimpleAuto extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor arm, slider;
    private Servo servo1;

    private final double COUNTS_PER_REV = 10.5*60;
    private final double WHEEL_DIAMETER_INCHES = 6.0;
    private final double COUNTS_PER_INCH = COUNTS_PER_REV / (Math.PI * WHEEL_DIAMETER_INCHES);
    private final double COUNTS_PER_DEGREE = 4000/360;

    @Override
    public void runOpMode() {
        // Hardware mapping
        frontLeft = hardwareMap.get(DcMotor.class, "fl_drive");
        frontRight = hardwareMap.get(DcMotor.class, "fr_drive");
        backLeft = hardwareMap.get(DcMotor.class, "bl_drive");
        backRight = hardwareMap.get(DcMotor.class, "br_drive");
        arm = hardwareMap.dcMotor.get("arm");
        slider = hardwareMap.dcMotor.get("slider");
        servo1 = hardwareMap.servo.get("servo1");

        // Set directions
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set zero power behavior
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slider.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Reset encoders
        resetEncoders();

        telemetry.addLine("Initialized. Waiting for start...");
        telemetry.update();

        waitForStart();
        closeClaw();
        moveArmToPosition(-1000, 1);

        moveToPosition(34,1);

        moveSliderToPosition(600,1);

        moveArmToPosition(-670, 1);
        sleep(500);

        openClaw();

        sleep(700);
        moveSliderToPosition(0,1);
        moveToPosition(-34, 0.7);

        strafeRight(-95, 0.7);

        moveToPosition(-20, 0.7);


        strafeRight(-2, 0.7);

        strafeRight(5, 0.7);

        moveArmToPosition(-170, 1);

        moveToPosition(28, 0.7);
        moveSliderToPosition(500, 1);


        closeClaw();
        sleep(700);

        moveArmToPosition(-1100, 1);

        moveToPosition(-30, 0.7);
        moveToPosition(5, 1);

        rotate(-135, 1);
        moveSliderToPosition(2750, 1);
        moveArmToPosition(-1030, 1);
        moveToPosition(5, 0.5);


        openClaw();
        sleep(700);

        moveArmToPosition(-1150, 1);
        moveSliderToPosition(0,1);
        moveArmToPosition(0,0.5);

        rotate(135, 1);
        moveToPosition(-7, 0.7);
        strafeRight(-7, 0.7);
        moveToPosition(-3, 0.7);
        strafeRight(20, 1);

        moveArmToPosition(-170, 1);

        moveToPosition(28, 0.7);
        moveSliderToPosition(500, 1);


        closeClaw();
        sleep(700);

        moveArmToPosition(-1100, 1);
        strafeRight(-20, 1);

        moveToPosition(-30, 0.7);
        moveToPosition(5, 1);

        rotate(-135, 1);
        moveSliderToPosition(2750, 1);
        moveArmToPosition(-1030, 1);
        moveToPosition(5, 0.5);


        openClaw();
        sleep(700);

        moveArmToPosition(-1150, 1);
        moveSliderToPosition(0,1);
        moveArmToPosition(0,0.5);

        sleep(700);






        //arm limits: 0(down) to -1100(up)
        //slider limits: 0 to 2200


        // moveArmToPosition(-500, 0.5);
        // moveToPosition(10, 0.5);
        // strafeRight(10, 0.5);
        // openClaw();
        // sleep(5000);

        // moveSliderToPosition(500, 0.5);
        // moveToPosition(10, 0.5);
        // closeClaw();
        //         sleep(5000);

        // rotate(360, 0.5);


    }

    private void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void moveToPosition(double inches, double power) {

        int moveCounts = (int) (inches * COUNTS_PER_INCH);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + moveCounts);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + moveCounts);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + moveCounts);
        backRight.setTargetPosition(backRight.getCurrentPosition() + moveCounts);

        runToPosition(power);
    }

    private void strafeRight(double inches, double power) {
        int moveCounts = (int) (inches * COUNTS_PER_INCH);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + moveCounts);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - moveCounts);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() - moveCounts);
        backRight.setTargetPosition(backRight.getCurrentPosition() + moveCounts);

        runToPosition(power);
    }

    private void rotate(double degrees, double power) {
        int moveCounts = (int) (degrees * COUNTS_PER_DEGREE);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + moveCounts);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() - moveCounts);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + moveCounts);
        backRight.setTargetPosition(backRight.getCurrentPosition() - moveCounts);

        runToPosition(power);
    }

    private void runToPosition(double power){

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive()){

            if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(frontLeft.getTargetPosition()) + 5 || Math.abs(frontLeft.getCurrentPosition()) < Math.abs(frontLeft.getTargetPosition()) -5 )
            {
                frontLeft.setPower(power);
                frontRight.setPower(power);
                backLeft.setPower(power);
                backRight.setPower(power);
            }
            else{
                stopMotors();
                break;
            }
            telemetry.addData("Current pos fl", (double)frontLeft.getCurrentPosition());
            telemetry.addData("Target pos fl", (double)frontLeft.getTargetPosition());
            telemetry.update();

        }
    }


    private void stopMotors() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void moveArmToPosition(int position, double power) {
        arm.setTargetPosition(position);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm.setPower(power);
    }

    private void moveSliderToPosition(int position, double power) {
        slider.setTargetPosition(position);
        slider.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while (opModeIsActive()){

            if (Math.abs(slider.getCurrentPosition()) > Math.abs(slider.getTargetPosition()) + 5 || Math.abs(slider.getCurrentPosition()) < Math.abs(slider.getTargetPosition()) -5 )
            {
                slider.setPower(power);;
            }
            else{
                slider.setPower(0);;
                break;
            }
        }
    }

    private void retractSlider(double power) {
        moveSliderToPosition(0, power);
    }

    private void openClaw() {
        servo1.setPosition(0.2);
    }

    private void closeClaw() {
        servo1.setPosition(0);
    }
}
