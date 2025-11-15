package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImplOnSimple;

@Autonomous
public class BartholomewAutonomous extends OpMode {
    DcMotor frontLeftMotor;
    DcMotor frontRightMotor;
    DcMotor rearLeftMotor;
    DcMotor rearRightMotor;
    DcMotor weedWacker;

    private static final byte I2C_ADDRESS = 0x70;
    private byte[] REGISTER_READ_DISTANCE = new byte[1];

    private I2cDeviceSynch sensor_fl;

    @Override
    public void init(){
        frontLeftMotor = hardwareMap.get(DcMotor.class, "fl_motor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "fr_motor");
        rearLeftMotor = hardwareMap.get(DcMotor.class, "rl_motor");
        rearRightMotor = hardwareMap.get(DcMotor.class, "rr_motor");
        weedWacker = hardwareMap.get(DcMotor.class, "weedwacker");

        sensor_fl = hardwareMap.get(I2cDeviceSynch.class, "sensor_fl");
        sensor_fl.engage();
        REGISTER_READ_DISTANCE[0] = (byte)0x51;
    }

    @Override
    public void loop(){
        sensor_fl.write(REGISTER_READ_DISTANCE);
        byte[] data = sensor_fl.read(2);
        if (data.length == 2){
            int distanceMm = (data[0] << 8) | data[1];
            telemetry.addData("Distance (mm)", distanceMm);
        }
        else{
            telemetry.addData("Distance(mm)", "Error");
        }
        telemetry.update();
    }
}
