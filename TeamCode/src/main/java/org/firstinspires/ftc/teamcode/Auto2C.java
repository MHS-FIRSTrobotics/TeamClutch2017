package org.firstinspires.ftc.teamcode;

//import com.qualcomm.ftccommon.configuration.EditPortListSpinnerActivity;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.ftccommunity.ftcxtensible.robot.ExtensibleGamepad;
import org.ftccommunity.ftcxtensible.util.JoystickQuestions;

import java.util.Objects;

@Autonomous
public class Auto2C extends LinearOpMode {
    static final String RED = "RED";
    static final String BLUE = "BLUE";
    String color;

    @Override
    public void runOpMode() {
        sleep(80);
        ClutchHardware hardware = new ClutchHardware(gamepad1, hardwareMap);
        hardware.driveTrain.configureDriveSystem(DriveTrain.DriveSystems.HOLONOMIC);
        ExtensibleGamepad xGamepad1 = new ExtensibleGamepad(gamepad1);
        JoystickQuestions questions = new JoystickQuestions(xGamepad1, telemetry);
        questions.addQuestion("FOO", "What color?", RED, BLUE);
        while (!isStarted() && !Thread.currentThread().isInterrupted()) {
            xGamepad1.updateGamepad(gamepad1);
            questions.loop();
            telemetry.update();
        }
        waitForStart();
        questions.stop();
        color = questions.responseTo("FOO") == null ? "RED" : questions.responseTo("FOO");
        telemetry.addData("COLOR", color).setRetained(true);
        ElapsedTime time = new ElapsedTime();
        while (opModeIsActive()) {
            double rawLightDetected = hardware.opticalDistanceSensor.getRawLightDetected();
            telemetry.addData("ODS_DATA", rawLightDetected);
            telemetry.update();

            if (rawLightDetected > .5) {
                break;
            }
            if (time.seconds() > 7)
                hardware.driveTrain.updateTarget(0, 0, 0);
            else if (time.seconds() > 3)
                hardware.driveTrain.updateTarget(0, .2, 0);
            else if (time.seconds() > 2.25)
                hardware.driveTrain.updateTarget(0, .4, 0);
            else if (time.seconds() > 1.5)
                hardware.driveTrain.updateTarget(0, .6, 0);
            else
                hardware.driveTrain.updateTarget(0, 1, 0);
        }
        telemetry.addData("STAGE", 2).setRetained(true);
        telemetry.update();
        while (opModeIsActive()) {
            double rawLightDetected = hardware.opticalDistanceSensor.getRawLightDetected();
            telemetry.addData("ODS_DATA", rawLightDetected);
            telemetry.update();

            if (hardware.distanceSensor.getDistance(DistanceUnit.CM) < 8) {
                hardware.driveTrain.updateTarget(0, 0, .05 * (color.equals(RED) ? -1 : 1));
                sleep(500);
                hardware.driveTrain.updateTarget(0, 0, 0);
                break;
            }
            if (rawLightDetected > .45) {
                hardware.driveTrain.updateTarget(0, .7, 0);
            } else {
                hardware.driveTrain.updateTarget(0, 0, .05 * (color.equals(RED) ? -1 : 1));
            }
        }
        telemetry.addData("STAGE", 3).setRetained(true);
        telemetry.update();
        time = new ElapsedTime();
        while (opModeIsActive()) {
            // left is red
            if (beaconClaim(hardware, time)) break;
        }

        hardware.driveTrain.updateTarget(0, 0, .1);
        sleep(500);
        time = new ElapsedTime();
        while (opModeIsActive()) {
            hardware.driveTrain.updateTarget(-.2, -.05, 0);
            if (time.seconds() > 3) {
                if (hardware.opticalDistanceSensor.getRawLightDetected() > .6) {
                    break;
                }
            }
        }
        telemetry.addData("STAGE", 4).setRetained(true);
        telemetry.update();
        while (opModeIsActive()) {
            double rawLightDetected = hardware.opticalDistanceSensor.getRawLightDetected();
            telemetry.addData("ODS_DATA", rawLightDetected);
            telemetry.update();

            if (hardware.distanceSensor.getDistance(DistanceUnit.CM) < 8) {
                hardware.driveTrain.updateTarget(0, 0, .05 * (color.equals(RED) ? -1 : 1));
                sleep(500);
                hardware.driveTrain.updateTarget(0, 0, 0);
                break;
            }
            if (rawLightDetected > .45) {
                hardware.driveTrain.updateTarget(0, .7, 0);
            } else {
                hardware.driveTrain.updateTarget(0, 0, .05 * (color.equals(RED) ? -1 : 1));
            }
        }
        telemetry.addData("STAGE", 5).setRetained(true);
        telemetry.update();
        time = new ElapsedTime();
        while (opModeIsActive()) {
            // left is red
            if (beaconClaim(hardware, time)) break;
        }
//        while (opModeIsActive()) {
//            hardware.driveTrain.updateTarget(.2, 0, 0);
//            if (time.seconds() > 4) {
//                if (hardware.opticalDistanceSensor.getRawLightDetected() > .6) {
//                    break;
//                }
//            }
//        }
        hardware.driveTrain.updateTarget(0, 0, 0);
    }

    private boolean beaconClaim(ClutchHardware hardware, ElapsedTime time) {
        if (time.seconds() < 2) {
            if (hardware.colorSensor.red() > hardware.colorSensor.blue()) {
                if (Objects.equals(color, RED)) {
                    driveForwardForBeacon(hardware);
                    sleep(700);
                    hardware.driveTrain.updateTarget(0, -.9, 0);
                    sleep(1000);
                    hardware.driveTrain.updateTarget(0, 0, 0);
                }
                driveForwardForBeacon(hardware);
                sleep(500);
            } else {
                if (Objects.equals(color, BLUE)) {
                    driveForwardForBeacon(hardware);
                    sleep(700);
                    hardware.driveTrain.updateTarget(0, -.9, 0);
                    sleep(1000);
                    hardware.driveTrain.updateTarget(0, 0, 0);
                }
                driveForwardForBeacon(hardware);
                sleep(500);
            }
        } else {
            hardware.driveTrain.updateTarget(0, 0, .05 * (color.equals(RED) ? -1 : 1));
            sleep(500);
            hardware.driveTrain.updateTarget(0, -.8, 0);
            sleep(900);
            hardware.driveTrain.updateTarget(0, 0, 0);
            return true;
        }
        return false;
    }

    private void driveForwardForBeacon(ClutchHardware hardware) {
        hardware.driveTrain.updateTarget(0, 1, 0);
        sleep(2500);
    }

    private void driveLeftForBeacon(ClutchHardware hardware) {
        hardware.driveTrain.updateTarget(.7 * (color.equals(RED) ? -1 : 1), 0, 0);
        sleep(1500);
    }
}
