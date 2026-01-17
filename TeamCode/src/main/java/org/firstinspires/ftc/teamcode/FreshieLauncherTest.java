package org.firstinspires.ftc.teamcode;

import androidx.core.math.MathUtils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="Freshman Launcher Test")
public class FreshieLauncherTest extends OpMode {
    int targetRPM = 0;
    final int MAX_RPM = 6000;
    final double TICKS_PER_REV = 28;
    DcMotorEx launchMotor = null;


    @Override
    public void init(){
        launchMotor = hardwareMap.get(DcMotorEx.class, "launchMotor");
        telemetry.addLine("Initialized");
        telemetry.update();
    }

    @Override
    public void loop(){
        if (gamepad1.dpadUpWasPressed()) { targetRPM += 500; }
        if (gamepad1.dpadDownWasPressed()) { targetRPM -= 500; }
        if (gamepad1.aWasPressed()) { targetRPM = 0; }
        targetRPM = MathUtils.clamp(targetRPM, -MAX_RPM, MAX_RPM);
        launchMotor.setVelocity(rpmToTps(targetRPM));

        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Actual RPM", tpsToRpm(launchMotor.getVelocity()));
        telemetry.update();
    }

    double rpmToTps(int rpm){
        return rpm * TICKS_PER_REV / 60d;
    }
    int tpsToRpm(double tps){
        return (int)(tps * 60 / TICKS_PER_REV);
    }
}
