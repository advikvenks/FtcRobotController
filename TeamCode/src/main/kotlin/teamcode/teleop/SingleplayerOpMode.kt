package teamcode.teleop

import LaunchBallsCommand
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import teamcode.commands.DefaultDriveCommand
import teamcode.commands.DefaultLauncherCommand
import teamcode.commands.IntakeCommand
import teamcode.commands.LoadLauncherCommand
import teamcode.subsystems.DriveSubsystem
import teamcode.subsystems.IntakeSubsystem
import teamcode.subsystems.LauncherSubsytem

@TeleOp(name = "Singleplayer Op Mode")
class SingleplayerOpMode : CommandOpMode() {
    private lateinit var backLeft: Motor
    private lateinit var backRight: Motor
    private lateinit var frontLeft: Motor
    private lateinit var frontRight: Motor

    private lateinit var intakeMotor: MotorEx
    private lateinit var launcherMotor: Motor
    private lateinit var launcherLoader: Motor

    private lateinit var driveGamepad: GamepadEx
    private lateinit var launchGamepad: GamepadEx


    private lateinit var longThreeLaunchButton: GamepadButton
    private lateinit var longOneLaunchButton: GamepadButton
    private lateinit var shortThreeLaunchButton: GamepadButton
    private lateinit var shortOneLaunchButton: GamepadButton

    private lateinit var loadButton: GamepadButton
    private fun initMotors() {
        backLeft = Motor(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312)
        backRight = Motor(hardwareMap, "backRight", Motor.GoBILDA.RPM_312)
        frontLeft = Motor(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_312)
        frontRight = Motor(hardwareMap, "frontRight", Motor.GoBILDA.RPM_312)

        intakeMotor = MotorEx(hardwareMap, "intakeMotor", Motor.GoBILDA.RPM_312)
        launcherMotor = Motor(hardwareMap, "launcherMotor")
        launcherLoader = Motor(hardwareMap, "launcherLoader", Motor.GoBILDA.RPM_312)

        backLeft.inverted = true
        frontLeft.inverted = true
        backRight.inverted = true
        frontRight.inverted = true

        launcherLoader.resetEncoder()
    }

    private fun initGamePads() {
        driveGamepad = GamepadEx(gamepad1)
        launchGamepad = GamepadEx(gamepad2)

        longThreeLaunchButton = GamepadButton(driveGamepad, GamepadKeys.Button.B)
        longOneLaunchButton = GamepadButton(driveGamepad, GamepadKeys.Button.A)
        shortThreeLaunchButton = GamepadButton(driveGamepad, GamepadKeys.Button.Y)
        shortOneLaunchButton = GamepadButton(driveGamepad, GamepadKeys.Button.X)

        loadButton = GamepadButton(driveGamepad, GamepadKeys.Button.DPAD_UP)
    }

    override fun initialize() {
        initMotors()
        initGamePads()

        val drive = DriveSubsystem(frontLeft, frontRight, backLeft, backRight)
        drive.defaultCommand = DefaultDriveCommand(drive, driveGamepad)

        val intake = IntakeSubsystem(intakeMotor, telemetry)

        val intakeCommand = IntakeCommand(intake, driveGamepad)
        intake.defaultCommand = intakeCommand   

        val launcher = LauncherSubsytem(launcherMotor, launcherLoader, telemetry)
        val defaultLauncherCommand = DefaultLauncherCommand(launcher, driveGamepad)

        val loadCommand = LoadLauncherCommand(launcher, telemetry)

        longThreeLaunchButton.whenPressed(LaunchBallsCommand(launcher, 1.0, 3))
        longOneLaunchButton.whenPressed(LaunchBallsCommand(launcher, 1.0, 1))
        shortThreeLaunchButton.whenPressed(LaunchBallsCommand(launcher, 0.8, 3))
        shortOneLaunchButton.whenPressed(LaunchBallsCommand(launcher, 0.8, 1))

        loadButton.whenPressed(loadCommand)
    }
}