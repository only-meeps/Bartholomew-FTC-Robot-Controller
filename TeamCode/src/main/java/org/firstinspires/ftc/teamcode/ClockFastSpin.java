package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Autonomous
public class ClockFastSpin extends LinearOpMode {
    DcMotorEx clockMotor;

    double currentVelocity = 0.1;

    boolean goingForward = true;

    @Override
    public void runOpMode(){
        clockMotor = hardwareMap.get(DcMotorEx.class, "clockMotor");
        clockMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        clockMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        waitForStart();
        while (opModeIsActive()){
            clockMotor.setVelocity(currentVelocity, AngleUnit.DEGREES);
            currentVelocity *= 1.001;
            if (currentVelocity > 27862){
                goingForward = !goingForward;
                clockMotor.setDirection(goingForward ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
                currentVelocity = 0.1;
            }
        }
    }
}
