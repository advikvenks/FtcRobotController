package teamcode

import LaunchThreeBalls
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.arcrobotics.ftclib.hardware.motors.Motor
import teamcode.commands.DefaultDriveCommand
import teamcode.subsystems.DriveSubsystem
import teamcode.subsystems.LauncherSubsytem


@TeleOp(name = "Experimental Op Mode")
class MainOpModeAdvanced : CommandOpMode() {
    private lateinit var backLeft: Motor
    private lateinit var backRight: Motor
    private lateinit var frontLeft: Motor
    private lateinit var frontRight: Motor

    private lateinit var intakeMotor: Motor
    private lateinit var launcherMotor: Motor
    private lateinit var launcherLoader: Motor

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

        launcherLoader.resetEncoder()
    }

    private fun initGamePads() {
        driveGamepad = GamepadEx(gamepad1)
        launchGamepad = GamepadEx(gamepad2)

        launchButton = GamepadButton(launchGamepad, GamepadKeys.Button.B)
        flickButton = GamepadButton(launchGamepad, GamepadKeys.Button.X)
        intakeButton = GamepadButton(launchGamepad, GamepadKeys.Button.A)
    }

    override fun initialize() {
        initMotors()
        initGamePads()

        val drive = DriveSubsystem(frontLeft, frontRight, backLeft, backRight)
        drive.defaultCommand = DefaultDriveCommand(drive, driveGamepad)

        val launcher = LauncherSubsytem(launcherMotor, launcherLoader, telemetry)
        val launchThreeBallsCommand = LaunchThreeBalls(launcher)

        launchButton.whenPressed(launchThreeBallsCommand)
    }


}