package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.components.MecanumDrive;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.components.NormalServo;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name="Mecanum Op Mode", group="FTC")
public class MecanumOpMode extends OpMode
{
    public List<Component> components = new ArrayList<Component>();

    protected MecanumDrive mecanum;

    DcMotor intake;
    DcMotor launcher;

    ServoController servoController;
    NormalServo rightPuncher;
    NormalServo leftPuncher;

    protected GyroSensor gyro;
    private OpticalDistanceSensor forward_dist;
    private OpticalDistanceSensor back_dist;

    //Gryo Strafe
    private int maintainHeading;
    private boolean headingSet = false;

    public void init()
    {
        //SENSORS
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();

        forward_dist = hardwareMap.opticalDistanceSensor.get("back_dist");
        forward_dist.enableLed(true);

        back_dist = hardwareMap.opticalDistanceSensor.get("forward_dist");
        back_dist.enableLed(true);

        //DRIVING
        DcMotor fl_motor = hardwareMap.dcMotor.get("motor_fl");
        DcMotor fr_motor = hardwareMap.dcMotor.get("motor_fr");
        DcMotor rl_motor = hardwareMap.dcMotor.get("motor_rl");
        DcMotor rr_motor = hardwareMap.dcMotor.get("motor_rr");

        intake = hardwareMap.dcMotor.get("intake");
        launcher = hardwareMap.dcMotor.get("launcher");

        fl_motor.setDirection(DcMotor.Direction.REVERSE);
        rl_motor.setDirection(DcMotor.Direction.REVERSE);

        mecanum = new MecanumDrive(fl_motor, fr_motor, rl_motor, rr_motor, gyro);

        //AUX MOTORS
        intake = hardwareMap.dcMotor.get("intake");
        launcher = hardwareMap.dcMotor.get("launcher");

        //PUNCHERS
        servoController = hardwareMap.servoController.get("Servo Controller 1");
        servoController.pwmEnable();

        rightPuncher = new NormalServo(servoController, 1);
        leftPuncher = new NormalServo(servoController, 2);

        //ADD ALL COMPONENTS
        components.add(mecanum);
        components.add(rightPuncher);
        components.add(leftPuncher);
    }

    public void loop()
    {
        //Joystick Movement
        mecanum.move(this.gamepad1.left_stick_x, this.gamepad1.left_stick_y, this.gamepad1.right_stick_x);

        //Gyro Assisted Strafing
        if(this.gamepad1.dpad_left || this.gamepad1.dpad_right){
            if(!headingSet){
                maintainHeading = gyro.getHeading();
                headingSet = true;
            }

            if(gamepad1.dpad_right){
                mecanum.move(1, 0, 0);
            }else{
                mecanum.move(-1, 0, 0);
            }

            mecanum.angleRotation(maintainHeading);
        }else{
            if(headingSet){
                headingSet = false;
            }
        }

        //Punchers
        if(this.gamepad1.left_bumper){
            leftPuncher.setLocation(1);
        }else{
            leftPuncher.setLocation(0);
        }

        if (this.gamepad1.right_bumper)
        {
            rightPuncher.setLocation(1);
        }else{
            rightPuncher.setLocation(0);
        }

        //Launcher
        if (this.gamepad1.x){
            launcher.setPower(1);
        }else if(this.gamepad1.y){
            launcher.setPower(-0.2);
        }else{
            launcher.setPower(0);
        }

        //Intake / Outake
        if(this.gamepad1.right_trigger > 0.1){
            intake.setPower(this.gamepad1.right_trigger);
        }else if(this.gamepad1.left_trigger > 0.1){
            intake.setPower(-this.gamepad1.left_trigger);
        }else{
            intake.setPower(0);
        }

        //Components
        for (Component component : components)
        {
            component.doit();
        }

        //Push data
        pushTelemetry();
    }

    public void pushTelemetry(){
        telemetry.addData("Gyro Heading", gyro.getHeading());
        telemetry.addData("Maintain Heading", maintainHeading);
        telemetry.addData("Forward Dist", forward_dist.getRawLightDetected());
        telemetry.addData("Back Dist", back_dist.getRawLightDetected());
        telemetry.addData("Drive Data",mecanum.getDataString());
    }
}