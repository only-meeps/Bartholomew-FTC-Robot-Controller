package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.time.ZoneId;

@Autonomous
public class MotorClock extends LinearOpMode {
    DcMotorEx clockMotor;

    final int ticksPerRevolution = (int)(28 * 99.5);

    @Override
    public void runOpMode() throws InterruptedException {
        clockMotor = hardwareMap.get(DcMotorEx.class, "clockMotor");

        waitForStart();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        int secondsWithinMinute = calendar.get(Calendar.SECOND);
        clockMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        clockMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        clockMotor.setPower(1);
        int secondStep = ticksPerRevolution / 60;
        while (opModeIsActive()) {
            clockMotor.setTargetPosition(secondStep * secondsWithinMinute);
        }
    }
}
