package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;


@Autonomous
public class Wheelie extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        IMU imu = hardwareMap.get(IMU.class, "imu");
        DcMotor rearLeftMotor = hardwareMap.get(DcMotor.class, "rl_motor");
        DcMotor rearRightMotor = hardwareMap.get(DcMotor.class, "rr_motor");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);
        waitForStart();

        Object myLock = new Object();
// ...
        synchronized (myLock) {
            myLock.wait(1000); // Works because the current thread holds the lock
        }
        telemetry.addLine("Please hold robot upright on the back wheels until this message disappears.");
        telemetry.update();
        synchronized (myLock) {
            myLock.wait(10000); // Works because the current thread holds the lock
        }
        telemetry.update();



        while (opModeIsActive()) {
            YawPitchRollAngles yawPitchRollAngles = imu.getRobotYawPitchRollAngles();
            telemetry.addData("Yaw", yawPitchRollAngles.getYaw());
            telemetry.addData("Pitch", yawPitchRollAngles.getPitch());
            telemetry.addData("Roll", yawPitchRollAngles.getRoll());
            telemetry.update();

            if (yawPitchRollAngles.getPitch() < -90){
                rearLeftMotor.setPower(1);
                rearRightMotor.setPower(1);
            }
            else if (yawPitchRollAngles.getPitch() > -90){
                rearLeftMotor.setPower(-1);
                rearRightMotor.setPower(-1);
            }
        }
    }
}
