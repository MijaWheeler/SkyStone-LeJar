/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

//for gyroscope:
//import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
//import com.qualcomm.robotcore.hardware.Gyroscope;
//import com.qualcomm.robotcore.hardware.IntegratingGyroscope;


@TeleOp(name="MecanumTank", group="Iterative Opmode")
public class GODMecanumTank extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //private ElapsedTime timer = new ElapsedTime();
    private DcMotor frontLeftDrive;
    private DcMotor frontRightDrive;
    private DcMotor backLeftDrive;
    private DcMotor backRightDrive;
    private DcMotor lift;
    private DcMotor claw;
    private Servo servoLeft;
    private Servo servoRight;


    //NavxMicroNavigationSensor navxMicro;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    // to 'get' must correspond to the names assigned during the robot configuration
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");


        // Setup motors
        frontLeftDrive  = hardwareMap.dcMotor.get("front_left_drive");
        frontRightDrive = hardwareMap.dcMotor.get("front_right_drive");
        backLeftDrive   = hardwareMap.dcMotor.get("back_left_drive");
        backRightDrive  = hardwareMap.dcMotor.get("back_right_drive");
        lift            = hardwareMap.dcMotor.get("Lift");
        claw            = hardwareMap.dcMotor.get("Claw");
        servoLeft=hardwareMap.servo.get("Servo_Left");
        servoRight=hardwareMap.servo.get("Servo_Right");

        // Set motor direction
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);

        //Disable encoders
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        claw.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        claw.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        // Setup gyro
        //navxMicro = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");

        // Wait until the gyro calibration is complete
        //timer.reset();
        //while (navxMicro.isCalibrating()) {
        //    telemetry.addData("calibrating", "%s", Math.round(timer.seconds()) % 2 == 0 ? "|.." : "..|");
        //    telemetry.update();
        //    Thread.sleep(500);
        //}
        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double magnitudeLeft = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
        double magnitudeRight = Math.hypot(gamepad1.right_stick_x, gamepad1.right_stick_y);
        double robotAngleLeft = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
        double robotAngleRight = Math.atan2(gamepad1.right_stick_y, gamepad1.right_stick_x) - Math.PI / 4;
        final double fld = magnitudeLeft * Math.sin(robotAngleLeft);
        final double frd = magnitudeRight * Math.cos(robotAngleRight);
        final double bld = magnitudeLeft * Math.cos(robotAngleLeft);
        final double brd = magnitudeRight * Math.sin(robotAngleRight);

        //Initial Motor Speeds
        frontLeftDrive.setPower(fld);
        frontRightDrive.setPower(frd);
        backLeftDrive.setPower(bld);
        backRightDrive.setPower(brd);
        claw.setPower(0.0);


        //Claw Controls
        double clawSpeed = 0.5;
        if (gamepad2.a)
            claw.setPower(clawSpeed);
        else if (gamepad2.b)
            claw.setPower(-clawSpeed);
        else
            claw.setPower(0.0);


        //Lift Controls
        double liftSpeed = 1;
        if (gamepad2.right_bumper)
            lift.setPower(liftSpeed);
        else if (gamepad2.left_bumper)
            lift.setPower(-liftSpeed);
        else
            lift.setPower(0.0);

        //Strafing
        if (gamepad1.right_bumper) { //right
            frontLeftDrive.setPower(-1);
            frontRightDrive.setPower(1);
            backLeftDrive.setPower(1);
            backRightDrive.setPower(-1);
        }
        else if (gamepad1.left_bumper) { //left
            frontLeftDrive.setPower(1);
            frontRightDrive.setPower(-1);
            backLeftDrive.setPower(-1);
            backRightDrive.setPower(1);
            }
        else {
            frontLeftDrive.setPower(fld);
            frontRightDrive.setPower(frd);
            backLeftDrive.setPower(bld);
            backRightDrive.setPower(brd);
        }

        //Servo Controls
        if (gamepad1.x) {
            servoLeft.setPosition(0.5);
            servoRight.setPosition(0.5);

        } else {
            servoLeft.setPosition(-0.5);
            servoRight.setPosition(-0.5);
        }



        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        // telemetry.addData("Motors", "left (%.2f), right (%.2f)", magnitude, robotAngle);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
