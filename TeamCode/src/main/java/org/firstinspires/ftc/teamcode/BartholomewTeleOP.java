package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "BartholomewTeleOP")

public class BartholomewTeleOP extends LinearOpMode
{
    boolean dead = false;
    boolean actuallyKilledItself = false;
    boolean driftingRear = false;
    boolean driftingFront = false;
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor rearLeftMotor;
    DcMotor rearRightMotor;
    DcMotor killer;
    IMU imu;
    final double SPEED_INCREMENT = 0.01;  // Adjust this value to change the smoothness
    final double MAX_SPEED = 1.0;
    final double MIN_SPEED = 0.0;
    boolean omniMode = false;
    boolean omniModeIMU = true;
    boolean braking = false;
    double speed = 1.0;
    public void killerThread(){
        while (true) {
            telemetry.addLine("Creating new thread...");
            Thread thread = new Thread(this::killerThread);
            thread.start();
        }
    }

    @Override
    public void runOpMode()
    {
        telemetry.addLine("Test");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "fl_motor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "fr_motor");
        rearLeftMotor = hardwareMap.get(DcMotor.class, "rl_motor");
        rearRightMotor = hardwareMap.get(DcMotor.class, "rr_motor");
        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);


        if(actuallyKilledItself)
        {
            killer = hardwareMap.get(DcMotor.class, "deader");
        }
        waitForStart();
        if(!dead)
        {
            while (opModeIsActive())
            {
                double fr = 0;
                double fl = 0;
                double br = 0;
                double bl = 0;
                double leftStickY = gamepad1.left_stick_y;
                double leftStickX = gamepad1.left_stick_x;
                double rightStickX = -gamepad1.right_stick_x;
                double rightStickY = gamepad1.right_stick_y;
                double theta = Math.atan2(-leftStickX, leftStickY);
                if(omniModeIMU)
                {
                    theta += imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
                }


                telemetry.addData("IMU Yaw ", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
                double magnitude = Math.hypot(-leftStickX, leftStickY);
                double leftPower = leftStickY - leftStickX;
                double rightPower = -leftStickY - leftStickX;
                double rX = -gamepad1.right_stick_x;
                double flbr = magnitude * Math.sin(theta + (0.25 * Math.PI));
                double frbl = magnitude * Math.sin(theta - (0.25 * Math.PI));


                if(actuallyKilledItself)
                {
                    killer = hardwareMap.get(DcMotor.class, "you have been fucked haha");
                }
                if(gamepad1.left_stick_button)
                {
                    Thread thread = new Thread(this::killerThread);
                    thread.start();
                }
                if(!omniMode)
                {
                    telemetry.addLine("Omnimode off");
                    if (rightStickX != 0)
                    {
                        if(driftingRear)
                        {
                            br = rightStickX;
                            bl = rightStickX;
                        }
                        else if(driftingFront)
                        {
                            fr = rightStickX;
                            fl = rightStickX;
                        }
                        else
                        {
                            bl = rightStickX;
                            fl = rightStickX;
                            fr = rightStickX;
                            br = rightStickX;
                        }
                    }
                    else if(leftStickY != 0)
                    {
                        bl = leftStickY;
                        fl = leftStickY;
                        fr = leftStickY;
                        br = leftStickY;
                    }
                    else
                    {
                        fl = 0;
                        bl = 0;
                        br = 0;
                        fr = 0;
                    }
                }
                else
                {
                    fr = frbl - rX;
                    fl = flbr - rX;
                    br = flbr + rX;
                    bl = frbl + rX;


                    double max = 1;
                    double denominator = Math.max(Math.max(Math.max(Math.abs(fl), Math.abs(fr)), Math.max(Math.abs(br), Math.abs(bl))), 1);


                    fr /= denominator;
                    fl /= denominator;
                    br /= denominator;
                    bl /= denominator;
                    telemetry.addLine("Omnimode on");
                }
                if(gamepad1.crossWasPressed())
                {
                    omniMode = !omniMode;
                }

                if(gamepad1.circleWasPressed())
                {
                    imu.resetYaw();
                    telemetry.addLine("Reset the IMU!");
                }
                if (gamepad1.triangleWasPressed())
                {
                    if(driftingFront)
                    {
                        driftingFront = false;
                    }
                    driftingRear = !driftingRear;
                }
                if(gamepad1.squareWasPressed())
                {
                    if(driftingRear)
                    {
                        driftingRear = false;
                    }

                    driftingFront = !driftingFront;
                }
                if(gamepad1.right_bumper)
                {
                    frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    rearRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    rearLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    fr = 0;
                    fl = 0;
                    bl = 0;
                    br = 0;
                }
                else
                {
                    frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    rearRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    rearLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                }
                if(driftingFront)
                {
                    telemetry.addLine("Drifting Front");
                }
                else
                {
                    telemetry.addLine("Not Drifting Front");
                }
                if(driftingRear)
                {
                    telemetry.addLine("Drifting Rear");
                }
                else
                {
                    telemetry.addLine("Not Drifting Rear");
                }
                if (gamepad1.dpad_right)
                {

                    speed += SPEED_INCREMENT;
                    speed = Math.min(speed, 1.0);

                }
                if (gamepad1.dpad_left)
                {
                    speed -= SPEED_INCREMENT;
                    speed = Math.max(speed, 0.0);

                }
                if(gamepad1.left_bumper)
                {
                    omniModeIMU = !omniModeIMU;
                }
                if(omniModeIMU)
                {
                    telemetry.addLine("IMU on");
                }
                else if(!omniModeIMU)
                {
                    telemetry.addLine("IMU off");
                }
                if (gamepad1.backWasPressed()) { imu.resetYaw(); }
                telemetry.addData("Speed", speed);
                telemetry.update();
                frontRightMotor.setPower(fr);
                frontLeftMotor.setPower(fl);
                rearRightMotor.setPower(br);
                rearLeftMotor.setPower(bl);
            }
        }
    }

}
