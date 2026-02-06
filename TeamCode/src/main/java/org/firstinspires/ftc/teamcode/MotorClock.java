package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.time.ZoneId;

@Autonomous
public class MotorClock extends LinearOpMode {
    DcMotorEx clockMotor;

    final double ticksPerRevolution = 5572.4;

    void clockTelemetry(int minutesPassed, int secondsWithinMinute){
        telemetry.addData("Minutes passed", minutesPassed);
        telemetry.addData("Seconds passed since last minute", secondsWithinMinute);
        telemetry.addData("Target position", clockMotor.getTargetPosition());
        telemetry.addData("Current position", clockMotor.getCurrentPosition());
        telemetry.update();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        clockMotor = hardwareMap.get(DcMotorEx.class, "clockMotor");

        waitForStart();

        clockMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        clockMotor.setTargetPosition(0);
        clockMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        clockMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        clockMotor.setPower(1);
        double secondStep = ticksPerRevolution / 60;
        int offset = 0;
        while (opModeIsActive()) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
            int secondsWithinMinute = calendar.get(Calendar.SECOND);
            if (secondsWithinMinute == 0){
                offset++;
            }

            clockMotor.setTargetPosition((int) ((int) -(secondStep * secondsWithinMinute) - (offset * secondStep * 60)));
            do {
                clockTelemetry(offset, secondsWithinMinute);
            } while (Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles")).get(Calendar.SECOND) == secondsWithinMinute);
            clockTelemetry(offset, secondsWithinMinute);
        }
    }
}
