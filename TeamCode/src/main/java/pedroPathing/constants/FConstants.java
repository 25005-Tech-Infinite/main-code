package pedroPathing.constants;

import com.pedropathing.localization.Localizers;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.util.CustomFilteredPIDFCoefficients;
import com.pedropathing.util.CustomPIDFCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class FConstants {
    static {
        FollowerConstants.localizers = Localizers.OTOS;

        FollowerConstants.leftFrontMotorName = "fl_drive";
        FollowerConstants.leftRearMotorName = "fr_drive";
        FollowerConstants.rightFrontMotorName = "bl_drive";
        FollowerConstants.rightRearMotorName = "br_drive";

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.FORWARD;

        FollowerConstants.mass = 8.2;

        FollowerConstants.xMovement = 57.61514498492865;
        FollowerConstants.yMovement = 45.514444666584644;

        FollowerConstants.forwardZeroPowerAcceleration = 23.84547739317407;
        FollowerConstants.lateralZeroPowerAcceleration = 120.2043597701381;

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(-0.5,0,-0.5,0);
        FollowerConstants.useSecondaryTranslationalPID = false;
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.1, 0, 0.02, 0);

        FollowerConstants.headingPIDFCoefficients.setCoefficients(0.7,0,0.4,0);
        FollowerConstants.useSecondaryHeadingPID = false;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(0.1, 0, 0.02, 0);

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.3,0,0.3,0.6,0);
        FollowerConstants.useSecondaryDrivePID = false;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.1, 0, 0.02, 0.2, 0);

        FollowerConstants.zeroPowerAccelerationMultiplier = 4;
        FollowerConstants.centripetalScaling = 0.05;

        FollowerConstants.pathEndTimeoutConstraint = 500;
        FollowerConstants.pathEndTValueConstraint = 0.995;
        FollowerConstants.pathEndVelocityConstraint = 0.1;
        FollowerConstants.pathEndTranslationalConstraint = 0.1;
        FollowerConstants.pathEndHeadingConstraint = 0.007;
    }
}
