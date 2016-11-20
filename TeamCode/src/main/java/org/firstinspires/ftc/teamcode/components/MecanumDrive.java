package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class MecanumDrive implements Component{

    private DcMotor fl_motor, fr_motor, rl_motor, rr_motor;
    private double x, y, rotation;

    private GyroSensor gyro;
    private double angleConstant = 0.04;
    private double angleMax = 0.3;

    public MecanumDrive(DcMotor fl_motor, DcMotor fr_motor, DcMotor rl_motor, DcMotor rr_motor){
        this.fl_motor = fl_motor;
        this.fr_motor = fr_motor;
        this.rl_motor = rl_motor;
        this.rr_motor = rr_motor;
    }

    public MecanumDrive(DcMotor fl_motor, DcMotor fr_motor, DcMotor rl_motor, DcMotor rr_motor, GyroSensor gyro){
        this.fl_motor = fl_motor;
        this.fr_motor = fr_motor;
        this.rl_motor = rl_motor;
        this.rr_motor = rr_motor;
        this.gyro = gyro;
        this.gyro.calibrate(); //Not sure if this is needed
    }

    public void move(double x, double y, double rotation){
        this.x = x;
        this.y = -y;
        this.rotation = rotation;
    }

    public void angleRotation(int angle){
        if(gyro == null){
            return;
        }

        //Converts to values between 0 and 359 to match modernrobotics gyro
        angle = angle % 360;
        if(angle < 0){
            angle = 360 + angle;
        }

        int dist = angle - gyro.getHeading(); //ASSUMES: rotation clockwise is positive

        //Reverse direction is other side is closer
        if(Math.abs(dist) > 180){
            if(dist < 0){
                dist = 360 + dist;
            }else if(dist > 0){
                dist = 360 - dist;
            }
        }

        if(Math.abs(dist) > 1){
            rotation = dist * angleConstant; //Proportional
            rotation = Math.max(Math.min(rotation, angleMax), -angleMax); //Floor and ceiling
        }
    }

    public void doit(){
        double fl_speed = x + y + rotation;
        double fr_speed = -x + y - rotation;
        double rl_speed = -x + y + rotation;
        double rr_speed = x + y - rotation;

        double max = 1;
        max = Math.max(max,fl_speed);
        max = Math.max(max,fr_speed);
        max = Math.max(max,rl_speed);
        max = Math.max(max,rr_speed);

        if(max > 1){
            fl_speed /= max;
            fr_speed /= max;
            rl_speed /= max;
            rr_speed /= max;
        }

        fl_motor.setPower(fl_speed);
        fr_motor.setPower(fr_speed);
        rl_motor.setPower(rl_speed);
        rr_motor.setPower(rr_speed);

        x = 0; y = 0; rotation =0;
    }
}
