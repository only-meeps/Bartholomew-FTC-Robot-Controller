package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.util.concurrent.TimeUnit;
@Autonomous(name = "BartholomewAutomonousMk2")
public class BartholomewAutonomousMk2 extends LinearOpMode {

    private final int READ_PERIOD = 1;

    //Inches
    private final int minDistance = 9;

    private final int minXSize = 210;
    private final int minYSize = 210;
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
        telemetry.addLine("Bart");

        Deadline rateLimit = new Deadline(READ_PERIOD, TimeUnit.SECONDS);


        rateLimit.expire();

        if (!huskyLens.knock()) {
            telemetry.addData(">>", "Problem communicating with " + huskyLens.getDeviceName());
        } else {
            huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
            telemetry.addData(">>", "Press start to continue");
        }

        huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            if (!rateLimit.hasExpired()) {
                continue;
            }
            rateLimit.reset();
            HuskyLens.Block[] blocks = huskyLens.blocks();

            // Get Robot Heading from IMU
            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            double robotHeading = angles.firstAngle;

            telemetry.addData("Robot Heading", "%.1f deg", robotHeading);
            telemetry.addData("Block count", blocks.length);

            if(blocks.length > 0){

            }
            for (int i = 0; i < blocks.length; i++) {
                telemetry.addData("Block", blocks[i].toString());
                double tagX = blocks[i].x;
                double tagY = blocks[i].y;
                double tagWidth = blocks[i].width;
                double tagHeight = blocks[i].height;
                double distortionX = blocks[i].height - blocks[i].width;
                //Angle
                distortionX = (distortionX * 360) / 320;
                double distance = ((distortionX * blocks[i].width) / minXSize) * minDistance;

                telemetry.addData("Block distortion X", distortionX);
                telemetry.addData("Block distance", distance);
            }
            telemetry.update();
        }
    }
}