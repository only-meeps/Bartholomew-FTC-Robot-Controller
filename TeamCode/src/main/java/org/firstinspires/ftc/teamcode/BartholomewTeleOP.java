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
                double leftStickY = gamepad1.left_stick_y;
                double leftStickX = gamepad1.left_stick_x;
                double rightStickX = -gamepad1.right_stick_x;
                double rightStickY = gamepad1.right_stick_y;
                double theta = Math.atan2(-leftStickX, leftStickY);
                theta += imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
                double magnitude = Math.hypot(-leftStickX, leftStickY);
                double leftPower = leftStickY - leftStickX;
                double rightPower = -leftStickY - leftStickX;
                double rX = -gamepad1.right_stick_x;
                double flbr = magnitude * Math.sin(theta + (0.25 * Math.PI));
                double frbl = magnitude * Math.sin(theta - (0.25 * Math.PI));

                double fr = frbl + rX;
                double fl = flbr + rX;
                double br = flbr + rX;
                double bl = frbl + rX;

                double max = 1;
                double denominator = Math.max(Math.max(Math.max(Math.abs(fl), Math.abs(fr)), Math.max(Math.abs(br), Math.abs(bl))), 1);


                fr /= denominator;
                fl /= denominator;
                br /= denominator;
                bl /= denominator;
                if(actuallyKilledItself)
                {
                    killer = hardwareMap.get(DcMotor.class, "you have been fucked haha");
                }
                if(gamepad1.left_stick_button)
                {
                    //actuallyKilledItself = true;
                }
                if(!omniMode)
                {
                    telemetry.addLine("Omnimode off");
                    if (rightStickX != 0)
                    {

                        if(driftingRear)
                        {
                            rearRightMotor.setPower(-rightStickX);
                            rearLeftMotor.setPower(-rightStickX);
                        }
                        else if(driftingFront)
                        {
                            frontLeftMotor.setPower(-rightStickX);
                            frontRightMotor.setPower(-rightStickX);
                        }
                        else
                        {
                            rearLeftMotor.setPower(rightStickX);
                            frontLeftMotor.setPower(rightStickX);
                            frontRightMotor.setPower(rightStickX);
                            rearRightMotor.setPower(rightStickX);
                        }

                    }
                    else if(leftStickY != 0)
                    {
                        if(driftingRear)
                        {
                            rearRightMotor.setPower(leftStickY);
                            rearLeftMotor.setPower(-leftStickY);
                        }
                        else if(driftingFront)
                        {
                            frontLeftMotor.setPower(-leftStickY);
                            frontRightMotor.setPower(leftStickY);
                        }
                        else
                        {
                            rearLeftMotor.setPower(-leftStickY);
                            frontLeftMotor.setPower(-leftStickY);
                            frontRightMotor.setPower(leftStickY);
                            rearRightMotor.setPower(leftStickY);
                        }

                    }
                    else
                    {
                        rearLeftMotor.setPower(0);
                        frontLeftMotor.setPower(0);
                        frontRightMotor.setPower(0);
                        rearRightMotor.setPower(0);
                    }
                }
                else
                {
                    telemetry.addLine("Omnimode on");
                    frontRightMotor.setPower(-fr);
                    frontLeftMotor.setPower(-fl);
                    rearRightMotor.setPower(br);
                    rearLeftMotor.setPower(bl);
                }
                if(gamepad1.crossWasPressed())
                {
                    omniMode = !omniMode;
                }

                //This is causing issues.
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
                if(driftingFront)
                {
                    telemetry.addLine("Drifting Front");
                }
                if(driftingRear)
                {
                    telemetry.addLine("Drifting Rear");
                }
                if (gamepad1.dpad_right)
                {
                    double speed = 0.5;
                    speed += SPEED_INCREMENT;
                    speed = Math.min(speed, 1.0);
                    leftPower = leftStickY + leftStickX * speed;
                    rightPower = -leftStickY + leftStickX * speed;

                }
                if (gamepad1.dpad_left)
                {
                    double speed = 0.5;
                    speed -= SPEED_INCREMENT;
                    speed = Math.max(speed, 0.0);
                    leftPower = leftStickY + leftStickX * speed;
                    rightPower = -leftStickY + leftStickX * speed;

                }

                if (gamepad1.backWasPressed()) { imu.resetYaw(); }

                telemetry.update();
            }
            rearLeftMotor.setPower(0.0);
            frontLeftMotor.setPower(0.0);
            rearRightMotor.setPower(0.0);
            frontRightMotor.setPower(0.0);
        }
    }

}
