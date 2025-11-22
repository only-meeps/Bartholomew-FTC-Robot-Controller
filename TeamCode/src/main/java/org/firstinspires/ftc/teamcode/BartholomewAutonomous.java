package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import java.util.Random;

@Autonomous
public class BartholomewAutonomous extends OpMode {
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor rearLeftMotor;
    DcMotor rearRightMotor;
    DcMotor weedWacker;

    private DistanceSensor sensor_rl;
    private DistanceSensor sensor_rr;
    private DistanceSensor sensor_fl;
    //private DistanceSensor sensor_fr;

    //hi

    private AutoMovementDirection currentMoveDirection;
    private AutoTurnDirection currentTurnDirection;

    private double rlDistance;
    private double rrDistance;
    private double flDistance;
    //private double frDistance;

    @Override
    public void init(){
        frontLeftMotor = hardwareMap.get(DcMotor.class, "fl_motor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "fr_motor");
        rearLeftMotor = hardwareMap.get(DcMotor.class, "rl_motor");
        rearRightMotor = hardwareMap.get(DcMotor.class, "rr_motor");
        weedWacker = hardwareMap.get(DcMotor.class, "weedwacker");

        sensor_rl = hardwareMap.get(DistanceSensor.class, "sensor_rl");
        sensor_rr = hardwareMap.get(DistanceSensor.class, "sensor_rr");
        sensor_fl = hardwareMap.get(DistanceSensor.class, "sensor_fl");
        //sensor_fr = hardwareMap.get(DistanceSensor.class, "sensor_fr");

    }

    private AutoMovementDirection getRandomDirection(){
        AutoMovementDirection[] directions = AutoMovementDirection.values();
        Random random = new Random();
        int index = random.nextInt(directions.length);
        return directions[index];
    }

    private AutoTurnDirection getRandomTurnDirection(){
        AutoTurnDirection[] directions = AutoTurnDirection.values();
        Random random = new Random();
        int index = random.nextInt(directions.length);
        return directions[index];
    }

    private boolean canMove(AutoMovementDirection direction){
        if (direction == AutoMovementDirection.Forward){
            return (flDistance > 20 /*&& frDistance > 20*/);
        }
        else if (direction == AutoMovementDirection.Back){
            return (rlDistance > 20 && rrDistance > 20);
        }
        else{
            return false;
        }
    }

    @Override
    public void loop(){
        rlDistance = sensor_rl.getDistance(DistanceUnit.CM);
        rrDistance = sensor_rr.getDistance(DistanceUnit.CM);
        flDistance = sensor_fl.getDistance(DistanceUnit.CM);
        //frDistance = sensor_fr.getDistance(DistanceUnit.CM);

        telemetry.addData("RL Sensor Distance:", rlDistance);
        telemetry.addData("RR Sensor Distance:", rrDistance);
        telemetry.addData("FL Sensor Distance:", flDistance);
        //telemetry.addData("FR Sensor Distance:", frDistance);
        telemetry.update();

        if (currentMoveDirection != null){
            if (currentMoveDirection == AutoMovementDirection.Forward){

            }
        }
        else{

        }
    }
}
