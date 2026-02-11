package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

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
    boolean dead = false;
    boolean actuallyKilledItself = false;
    boolean driftingRear = false;
    boolean driftingFront = false;
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor rearLeftMotor;
    DcMotor rearRightMotor;
    DcMotor killer;
    final double SPEED_INCREMENT = 0.01;  // Adjust this value to change the smoothness
    final double MAX_SPEED = 1.0;
    final double MIN_SPEED = 0.0;
    boolean omniMode = false;

    @Override
    public void runOpMode()
    {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "fl_motor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "fr_motor");
        rearLeftMotor = hardwareMap.get(DcMotor.class, "rl_motor");
        rearRightMotor = hardwareMap.get(DcMotor.class, "rr_motor");

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

                double leftPower = leftStickY + leftStickX;
                double rightPower = -leftStickY + leftStickX;
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
                            rearLeftMotor.setPower(-rightStickX);
                            frontLeftMotor.setPower(-rightStickX);
                            frontRightMotor.setPower(-rightStickX);
                            rearRightMotor.setPower(-rightStickX);
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

                }
                if(gamepad1.crossWasPressed())
                {
                    omniMode = !omniMode;
                }

                if (gamepad1.right_trigger > 0.3)
                {
                    rearLeftMotor.setPower(-gamepad1.right_trigger);
                    frontLeftMotor.setPower(-gamepad1.right_trigger);
                    frontRightMotor.setPower(-gamepad1.right_trigger);
                    rearRightMotor.setPower(gamepad1.right_trigger);
                }
                if (gamepad1.left_trigger > 0.3)
                {
                    rearLeftMotor.setPower(gamepad1.left_trigger);
                    frontLeftMotor.setPower(gamepad1.left_trigger);
                    frontRightMotor.setPower(gamepad1.left_trigger);
                    rearRightMotor.setPower(-gamepad1.left_trigger);
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
            }
            rearLeftMotor.setPower(0.0);
            frontLeftMotor.setPower(0.0);
            rearRightMotor.setPower(0.0);
            frontRightMotor.setPower(0.0);
        }
    }

}
