package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "HuskyLens Field Positioning")
public class BartholomewAutonomousMk2 extends LinearOpMode {

    private HuskyLens huskyLens;
    private BNO055IMU imu;

    // --- CONFIGURATION CONSTANTS ---
    // You must calibrate these! See instructions below.
    // Ideally, place the robot exactly 10 inches from a tag and measure the pixel width.
    // FOCAL_LENGTH = (PixelWidth * DistanceInches) / RealTagWidthInches
    // Example: (50 pixels * 10 inches) / 2 inches = 250
    private final double FOCAL_LENGTH_PIXELS = 220.0;
    private final double REAL_TAG_WIDTH_INCHES = 2.0; // Standard small AprilTags are 2 inches

    // The center of the HuskyLens image (320x240 resolution)
    private final double IMAGE_CENTER_X = 160.0;

    @Override
    public void runOpMode() {
        // 1. Initialize HuskyLens
        huskyLens = hardwareMap.get(HuskyLens.class, "huskylens");

        // 2. Initialize IMU (Gyro) inside the Control Hub
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);

        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
            telemetry.addData(">>", "Press start to continue");
        }
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            HuskyLens.Block[] blocks = huskyLens.blocks();

            // Get Robot Heading from IMU
            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double robotHeading = angles.firstAngle;

            telemetry.addData("Robot Heading", "%.1f deg", robotHeading);
            telemetry.addData("Block count", blocks.length);

            for (int i = 0; i < blocks.length; i++) {
                // --- MATH SECTION ---

                // 1. Calculate Distance (d)
                // The closer the tag, the wider it looks in pixels.
                double detectedWidth = blocks[i].width;
                double distanceFromTag = (FOCAL_LENGTH_PIXELS * REAL_TAG_WIDTH_INCHES) / detectedWidth;

                // 2. Calculate Bearing (theta)
                // How far left/right is the tag from the center of the screen?
                // We assume a Field of View (FOV) roughly related to coordinates.
                // HuskyLens X goes 0 to 320.
                double xOffset = blocks[i].x - IMAGE_CENTER_X;
                // A rough approximation: 1 pixel approx 0.18 degrees (depends on lens)
                double bearingToTag = xOffset * -0.18;

                // 3. Calculate Field Position (Simple 1-Tag Example)
                // We need to know WHICH tag this is to know where we are.
                // Let's assume Tag ID 1 is at Field Position (0, 72) (Center of back wall)
                double tagFieldX = 0;
                double tagFieldY = 72;

                if(blocks[i].id == 1) {
                    // Calculate absolute angle of the vector from Robot to Tag
                    double absoluteAngleToTag = robotHeading + bearingToTag;

                    // Convert polar coordinates (distance, angle) to Cartesian (x, y) offset
                    double xOffsetFromTag = distanceFromTag * Math.sin(Math.toRadians(absoluteAngleToTag));
                    double yOffsetFromTag = distanceFromTag * Math.cos(Math.toRadians(absoluteAngleToTag));

                    // Robot Position = Tag Position - Offset
                    double robotX = tagFieldX - xOffsetFromTag;
                    double robotY = tagFieldY - yOffsetFromTag;

                    telemetry.addData("--- Tag Detected", "ID: %d", blocks[i].id);
                    telemetry.addData("Dist to Tag", "%.2f in", distanceFromTag);
                    telemetry.addData("Bearing", "%.2f deg", bearingToTag);
                    telemetry.addData("Calculated Pos", "X: %.1f, Y: %.1f", robotX, robotY);
                }
            }
            telemetry.update();
        }
    }
}