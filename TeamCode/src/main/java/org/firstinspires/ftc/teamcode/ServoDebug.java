package org.firstinspires.ftc.teamcode;

import androidx.core.math.MathUtils;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@TeleOp()
public class ServoDebug extends LinearOpMode {
    @Override
    public void runOpMode(){
        Servo gate = hardwareMap.get(Servo.class, "gate");
        double currentPos = 0.5;
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.dpadDownWasReleased()){
                currentPos = 0.197;
            }
            else if (gamepad1.dpadUpWasReleased()){
                currentPos = 0.524;
            }
            currentPos = MathUtils.clamp(currentPos, 0, 1);
            gate.setPosition(currentPos);
            telemetry.addData("Servo Pos", currentPos);
            telemetry.update();
        }
    }

}
