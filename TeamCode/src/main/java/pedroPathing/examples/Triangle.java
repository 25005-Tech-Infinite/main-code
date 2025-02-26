package pedroPathing.examples;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


@Autonomous(name = "Simple One Path Auto", group = "Examples")
public class Triangle extends OpMode {

    private Follower follower;
    private int pathState;

    // Start and End Poses
    private final Pose startPose = new Pose(8, 72.3, Math.toRadians(90));  // Starting position
    private final Pose endPose = new Pose(14, 129, Math.toRadians(90));      // Ending position

    // Control Point for Bezier Curve (influences the curve between start and end)
    private final Pose controlPose = new Pose(20, 100, Math.toRadians(0));

    // Single Path
    private Path simpleCurvePath;

    @Override
    public void init() {
        follower = new Follower(hardwareMap);

        buildPaths();   // Build the path before starting
        pathState = 0;  // Initialize state
    }

    /**
     * Builds a single BezierCurve path from startPose to endPose.
     */
    public void buildPaths() {
        simpleCurvePath = new Path(
                new BezierCurve(
                        new Point(startPose),     // Start Point
                        new Point(controlPose),   // Control Point (manipulates curvature)
                        new Point(endPose)        // End Point
                )
        );

        // Linear heading interpolation: gradually changes heading along the path
        simpleCurvePath.setLinearHeadingInterpolation(
                startPose.getHeading(),
                endPose.getHeading()
        );
    }

    @Override
    public void loop() {
        autonomousPathUpdate();  // Call path update logic continuously
    }

    /**
     * Runs the path-following logic with a simple 2-state system.
     */
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Start following the path
                follower.followPath(simpleCurvePath);

                pathState = 1;
                break;

            case 1:
                // Wait until the follower completes the path
                if (!follower.isBusy()) {
                    telemetry.addLine("Path Completed!");
                    telemetry.update();
                } else {
                    telemetry.addData("Following Path", true);
                    telemetry.update();
                }
                break;
        }
    }
}
