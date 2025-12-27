/* Copyright (c) 2021 FIRST. All rights reserved.
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
package teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.max

@Suppress("unused")
@TeleOp(name = "Main OpMode", group = "Linear OpMode")
class MainOpMode : LinearOpMode() {
    private val runtime = ElapsedTime()
    private lateinit var frontLeftDrive: DcMotor
    private lateinit var backLeftDrive: DcMotor
    private lateinit var frontRightDrive: DcMotor
    private lateinit var backRightDrive: DcMotor

    private lateinit var intakeMotor: DcMotor

    private lateinit var launcherMotor: DcMotor

    private lateinit var flickerMotor: DcMotor

    //    This function initializes all the devices like servos and motors and stuff
//    To add motors, go to the driver station app > menu > configure > control hub portal > (control hub/expansion hub) > type of device > port and name
//    Spam back, then save when prompted
//    Then, declare it in a variable up here
//    After that, look at the comment in this function
    private fun initDevices() {
        frontLeftDrive = hardwareMap.get(DcMotor::class.java, "frontLeft")
        backLeftDrive = hardwareMap.get(DcMotor::class.java, "backLeft")
        frontRightDrive = hardwareMap.get(DcMotor::class.java, "frontRight")
        backRightDrive = hardwareMap.get(DcMotor::class.java, "backRight")

        intakeMotor = hardwareMap.get(DcMotor::class.java, "intakeMotor")

        launcherMotor = hardwareMap.get(DcMotor::class.java, "launcherMotor")

        flickerMotor = hardwareMap.get(DcMotor::class.java, "launcherLoader")


        frontLeftDrive.direction = DcMotorSimple.Direction.REVERSE
        backLeftDrive.direction = DcMotorSimple.Direction.REVERSE
        frontRightDrive.direction = DcMotorSimple.Direction.FORWARD
        backRightDrive.direction = DcMotorSimple.Direction.FORWARD
    }

    //You probably should not have to modify this function as it is the main drive logic
    private fun doMecanumDrive(axial: Double, lateral: Double, yaw: Double) {
        var max: Double

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        var frontLeftPower = axial + lateral + yaw
        var frontRightPower = axial - lateral - yaw
        var backLeftPower = axial - lateral + yaw
        var backRightPower = axial + lateral - yaw

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = max(abs(frontLeftPower), abs(frontRightPower))
        max = max(max, abs(backLeftPower))
        max = max(max, abs(backRightPower))

        if (max > 1.0) {
            frontLeftPower /= max
            frontRightPower /= max
            backLeftPower /= max
            backRightPower /= max
        }

        // Send calculated power to wheels
        frontLeftDrive.power = frontLeftPower
        frontRightDrive.power = frontRightPower
        backLeftDrive.power = backLeftPower
        backRightDrive.power = backRightPower

        // Show the elapsed game time and wheel power.
        telemetry.addData("Info", "Run Time: $runtime")
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower)
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower)
    }

    private fun controlIntake() {
        if (gamepad2.cross) {
            intakeMotor.power = 0.4
        } else {
            intakeMotor.power = 0.0
        }
    }

    private fun controlLauncher() {
        if (gamepad2.triangle) {
            launcherMotor.power = 1.0
        } else {
            launcherMotor.power = 0.0
        }
    }

    private fun speedUpLauncher() {
        val startTime = runtime.seconds()

        launcherMotor.power = .5 //starting power

        while (runtime.seconds() - startTime < 0.5) {
            val elapsedTime = runtime.seconds() - startTime

            val newPower = 0.5 + elapsedTime
            launcherMotor.power = newPower
        }

        launcherMotor.power = 1.0
    }

    private fun launcherFlick() {
        flickerMotor.power = 0.35
        smartSleep(.1)
        flickerMotor.power = 0.0

        smartSleep(.2)

        flickerMotor.power = -0.35
        smartSleep(.1)
        flickerMotor.power = 0.0
    }

    private fun smartSleep(seconds: Double) {
        val timer = ElapsedTime()
        while (timer.seconds() < seconds && opModeIsActive()) {

        }
    }

    private fun slowDownLauncher() {
        val startTime = runtime.seconds()

        while (runtime.seconds() - startTime < 0.5) {
            val elapsedTime = runtime.seconds() - startTime

            val newPower = 1.0 - (elapsedTime * 2)
            launcherMotor.power = newPower
        }

        launcherMotor.power = 0.0
    }

    override fun runOpMode() {

        initDevices() //initialize the motors and servos and stuff
        telemetry.addData("Status", "Initialized")
        telemetry.update()

        waitForStart()
        runtime.reset()

        //This is the main control loop and basically is a loop that runs until we end the opmode
        while (opModeIsActive()) {
            val axial = -gamepad1.left_stick_y.toDouble() // Note: pushing stick forward gives negative value
            val lateral = gamepad1.left_stick_x.toDouble()
            val yaw = gamepad1.right_stick_x.toDouble()

            doMecanumDrive(axial, lateral, yaw)

            controlIntake()
            controlLauncher()

            if (gamepad2.left_bumper) {
                speedUpLauncher()
            }

            if (gamepad2.right_bumper) {
                slowDownLauncher()
            }

            if (gamepad2.squareWasReleased()) {
                launcherFlick()
            }

            telemetry.update()
        }
    }
}
