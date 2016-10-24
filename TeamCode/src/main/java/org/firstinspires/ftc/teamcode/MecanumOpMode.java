package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.components.MecanumDrive;
import org.firstinspires.ftc.teamcode.components.Component;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Mecanum Op Mode", group="FTC")
public class MecanumOpMode extends OpMode
{
    public List<Component> components = new ArrayList<Component>();

    MecanumDrive mecanum;

    public void init()
    {
        //DRIVING
        DcMotor fl_motor = hardwareMap.dcMotor.get("motor_fl");
        DcMotor fr_motor = hardwareMap.dcMotor.get("motor_fr");
        DcMotor rl_motor = hardwareMap.dcMotor.get("motor_rl");
        DcMotor rr_motor = hardwareMap.dcMotor.get("motor_rr");


        rl_motor.setDirection(DcMotor.Direction.REVERSE);
        rr_motor.setDirection(DcMotor.Direction.REVERSE);

        mecanum = new MecanumDrive(fl_motor, fr_motor, rl_motor, rr_motor);

        components.add(mecanum);
    }

    public void loop()
    {
        mecanum.move(this.gamepad1.left_stick_y, this.gamepad1.left_stick_x , this.gamepad1.right_stick_x);

        telemetry.addData("Mecanum Speeds", mecanum.getSpeedString());

        for (Component component : components)
        {
            component.doit();
        }
    }
}