package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MecanumOpMode;

/**
 * Created by fendleyc on 11/21/16.
 */
@Autonomous(name="RotationTest", group="FTC")

public class RotationTest extends StatefulAutonomous{
    @State(first = true)
    public void rotate(){
        this.mecanum.angleRotation(45);

        if(this.mecanum.onAngle(45)){
            maintainHeading = gyro.getHeading();
            setNextState("strafe");
        }
    }

    private int maintainHeading;
    @State(timed = true, duration = 3.0, nextState = "done")
    public void strafe(){

        this.mecanum.move(-1, 0, 0);
        this.mecanum.angleRotation(maintainHeading);
    }

    @State
    public void done(){

    }

}
