package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/*
 * This OpMode ramps a single motor speed up and down repeatedly until Stop is pressed.
 * The code is structured as a LinearOpMode
 *
 * This code assumes a DC motor configured with the name "left_drive" as is found on a Robot.
 *
 * INCREMENT sets how much to increase/decrease the power each cycle
 * CYCLE_MS sets the update period.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */
@TeleOp(name = "BartholomewTeleOPl")

public class BartholomewTeleOP extends LinearOpMode
{
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor rearLeftMotor;
    DcMotor rearRightMotor;
    DcMotor weedWacker;
    double weedWackerSpeed = 0.0;
    final double SPEED_INCREMENT = 0.01;  // Adjust this value to change the smoothness
    final double MAX_SPEED = 1.0;
    final double MIN_SPEED = 0.0;

    @Override
    public void runOpMode()
    {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "fl_motor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "fr_motor");
        rearLeftMotor = hardwareMap.get(DcMotor.class, "rl_motor");
        rearRightMotor = hardwareMap.get(DcMotor.class, "rr_motor");
        weedWacker = hardwareMap.get(DcMotor.class, "weedwacker");

        waitForStart();

        while (opModeIsActive())
        {
            double leftStickY = gamepad1.left_stick_y;
            double leftStickX = -gamepad1.left_stick_x;
            double rightStickX = -gamepad1.right_stick_x;
            double rightStickY = gamepad1.right_stick_y;

            double leftPower = leftStickY + leftStickX * 0.5;
            double rightPower = -leftStickY + leftStickX * 0.5;

            if (leftStickX > 0)
            {
                rearLeftMotor.setPower(leftPower);
                frontLeftMotor.setPower(leftPower);
                frontRightMotor.setPower(rightPower);
                rearRightMotor.setPower(-rightPower);
            }
            else if (leftStickX < 0)
            {
                rearLeftMotor.setPower(leftPower);
                frontLeftMotor.setPower(leftPower);
                rearRightMotor.setPower(-rightPower);
                frontRightMotor.setPower(rightPower);
            }
            else
            {
                rearLeftMotor.setPower(leftPower);
                frontLeftMotor.setPower(leftPower);
                rearRightMotor.setPower(-rightPower);
                frontRightMotor.setPower(rightPower);
            }

            if (gamepad1.right_bumper)
            {
                weedWacker.setPower(weedWackerSpeed);
            }
            else if (gamepad1.left_bumper)
            {
                weedWacker.setPower(-weedWackerSpeed);
            }
            else
            {
                weedWacker.setPower(0.0);
            }

            //Smooth speed changes
            if (gamepad1.dpad_up && weedWackerSpeed < MAX_SPEED)
            {
                weedWackerSpeed += SPEED_INCREMENT;
                weedWackerSpeed = Math.min(weedWackerSpeed, MAX_SPEED); //Ensure it doesn't exceed max
            }
            else if (gamepad1.dpad_down && weedWackerSpeed > MIN_SPEED)
            {
                weedWackerSpeed -= SPEED_INCREMENT;
                weedWackerSpeed = Math.max(weedWackerSpeed, MIN_SPEED); //Ensure it doesn't go below min
            }

            if (gamepad1.right_trigger > 0.3)
            {
                rearRightMotor.setPower(1.0);
                frontRightMotor.setPower(1.0);
            }
            if (gamepad1.left_trigger > 0.3)
            {
                rearLeftMotor.setPower(-1.0);
                frontLeftMotor.setPower(-1.0);
            }

            //This is causing issues.
            if (gamepad1.triangle)
            {
                rearLeftMotor.setPower(1);
                frontLeftMotor.setPower(1);
                rearRightMotor.setPower(-1);
                frontRightMotor.setPower(-1);
            }

            if (gamepad1.dpad_right && 0.5 < 1)
            {
                double speed = 0.5;
                speed += SPEED_INCREMENT;
                speed = Math.min(speed, 1.0);
                leftPower = leftStickY + leftStickX * speed;
                rightPower = -leftStickY + leftStickX * speed;

            }
            if (gamepad1.dpad_left && 0.5 > 0)
            {
                double speed = 0.5;
                speed -= SPEED_INCREMENT;
                speed = Math.max(speed, 0.0);
                leftPower = leftStickY + leftStickX * speed;
                rightPower = -leftStickY + leftStickX * speed;

            }
        }
        rearLeftMotor.setPower(0.0);
        frontLeftMotor.setPower(0.0);
        rearRightMotor.setPower(0.0);
        frontRightMotor.setPower(0.0);
    }
}
