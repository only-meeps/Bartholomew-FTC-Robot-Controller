package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class Wheelie extends LinearOpMode {
    @Override
    public void runOpMode(){
        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);

        while (opModeIsActive()) {
            YawPitchRollAngles yawPitchRollAngles = imu.getRobotYawPitchRollAngles();
            telemetry.addLine("IMU initialized.");
            telemetry.addData("Yaw", yawPitchRollAngles.getYaw());
            telemetry.addData("Pitch", yawPitchRollAngles.getPitch());
            telemetry.addData("Roll", yawPitchRollAngles.getRoll());
            telemetry.update();
        }
    }
}
