package teamcode

import com.arcrobotics.ftclib.command.InstantCommand
import com.arcrobotics.ftclib.command.button.Button
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.controller.PIDController
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.hardware.HardwareMap


@TeleOp(name = "Experimental Op Mode")
class MainOpModeAdvanced : LinearOpMode() {
    private lateinit var backLeft: Motor
    private lateinit var backRight: Motor
    private lateinit var frontLeft: Motor
    private lateinit var frontRight: Motor

    private lateinit var intakeMotor: Motor
    private lateinit var launcherMotor: Motor
    private lateinit var launcherLoader: Motor

    private val targetRPM = 5000

    private lateinit var driveGamepad: GamepadEx
    private lateinit var launchGamepad: GamepadEx


    private lateinit var launchButton: GamepadButton
    private lateinit var flickButton: GamepadButton
    private lateinit var intakeButton: GamepadButton
    private fun initMotors() {
        backLeft = Motor(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312)
        backRight = Motor(hardwareMap, "backRight", Motor.GoBILDA.RPM_312)
        frontLeft = Motor(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_312)
        frontRight = Motor(hardwareMap, "frontRight", Motor.GoBILDA.RPM_312)

        intakeMotor = Motor(hardwareMap, "intakeMotor", Motor.GoBILDA.RPM_312)

        launcherMotor = Motor(hardwareMap, "launcherMotor")

        launcherLoader = Motor(hardwareMap, "launcherLoader", Motor.GoBILDA.RPM_312)

        backLeft.inverted = true
        frontLeft.inverted = true
    }

    private fun initGamePads() {
        driveGamepad = GamepadEx(gamepad1)
        launchGamepad = GamepadEx(gamepad2)

        launchButton = GamepadButton(launchGamepad, GamepadKeys.Button.Y)
        flickButton = GamepadButton(launchGamepad, GamepadKeys.Button.X)
        intakeButton = GamepadButton(launchGamepad, GamepadKeys.Button.A)
    }

    override fun runOpMode() {

        initMotors()
        initGamePads()

        val launcher = Launcher(launcherMotor as MotorEx, launcherLoader)
        val launchThreeBallsCommand = LaunchThreeBalls(launcher)

        val drive = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

        waitForStart()

        while (opModeIsActive()) {
            val x = driveGamepad.leftX
            val y = driveGamepad.leftY
            val rx = driveGamepad.rightX

            drive.driveRobotCentric(x, y, rx)

            launchButton.whenPressed(launchThreeBallsCommand)
        }
    }
}