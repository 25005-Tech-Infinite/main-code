//package pedroPathing.examples;
//
//import com.pedropathing.pathgen.BezierLine;
//import com.pedropathing.pathgen.PathBuilder;
//import com.pedropathing.pathgen.Point;
//
//public class Pathing {
//
//    public GeneratedPath() {
//        PathBuilder builder = new PathBuilder();
//
//        builder
//                .addPath(
//                        // Line 1
//                        new BezierLine(
//                                new Point(8.000, 72.300, Point.CARTESIAN),
//                                new Point(42.400, 72.000, Point.CARTESIAN)
//                        )
//                )
//                .setLinearHeadingInterpolation(
//                        Math.toRadians(0),
//                        Math.toRadians(0)
//                )
//                .addPath(
//                        // Line 2
//                        new BezierLine(
//                                new Point(42.400, 72.000, Point.CARTESIAN),
//                                new Point(45.500, 120.800, Point.CARTESIAN)
//                        )
//                )
//                .setTangentHeadingInterpolation()
//                .addPath(
//                        // Line 3
//                        new BezierLine(
//                                new Point(45.500, 120.800, Point.CARTESIAN),
//                                new Point(9.400, 134.500, Point.CARTESIAN)
//                        )
//                )
//                .setLinearHeadingInterpolation(
//                        Math.toRadians(0),
//                        Math.toRadians(0)
//                )
//                .addPath(
//                        // Line 4
//                        new BezierLine(
//                                new Point(9.400, 134.500, Point.CARTESIAN),
//                                new Point(46.700, 131.700, Point.CARTESIAN)
//                        )
//                )
//                .setTangentHeadingInterpolation()
//                .addPath(
//                        // Line 5
//                        new BezierLine(
//                                new Point(46.700, 131.700, Point.CARTESIAN),
//                                new Point(9.400, 134.500, Point.CARTESIAN)
//                        )
//                )
//                .setTangentHeadingInterpolation()
//                .addPath(
//                        // Line 6
//                        new BezierLine(
//                                new Point(9.400, 134.500, Point.CARTESIAN),
//                                new Point(46.100, 142.100, Point.CARTESIAN)
//                        )
//                )
//                .setTangentHeadingInterpolation()
//                .addPath(
//                        // Line 7
//                        new BezierLine(
//                                new Point(46.100, 142.100, Point.CARTESIAN),
//                                new Point(9.400, 134.500, Point.CARTESIAN)
//                        )
//                )
//                .setTangentHeadingInterpolation();
//    }
//}
