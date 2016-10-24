package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumDrive implements Component{

    private DcMotor fl_motor, fr_motor, rl_motor, rr_motor;
    private double fl_speed, fr_speed, rl_speed, rr_speed;

    public MecanumDrive(DcMotor fl_motor, DcMotor fr_motor, DcMotor rl_motor, DcMotor rr_motor){
        this.fl_motor = fl_motor;
        this.fr_motor = fr_motor;
        this.rl_motor = rl_motor;
        this.rr_motor = rr_motor;
    }

    public void move(double x, double y, double rotation){
        double xIn = x;
        double yIn = -y;

        fl_speed = xIn + yIn - rotation;
        fr_speed = -xIn + yIn - rotation;
        rl_speed = -xIn + yIn + rotation;
        rr_speed = xIn + yIn + rotation;

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
    }

    public String getSpeedString(){
        return "fl: " + fl_speed+" fr: "+fr_speed+" rl: "+rl_speed+" rr:"+rr_speed;
    }

    public void doit(){
        fl_motor.setPower(fl_speed);
        fr_motor.setPower(fr_speed);
        rl_motor.setPower(rl_speed);
        rr_motor.setPower(rr_speed);

        fl_speed = 0; fr_speed = 0; rl_speed = 0; rr_speed = 0;
    }
}
