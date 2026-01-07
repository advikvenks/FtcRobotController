package teamcode.teleop

import LaunchThreeBalls
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import teamcode.commands.DefaultDriveCommand
import teamcode.commands.IntakeCommand
import teamcode.commands.LoadLauncherCommand
import teamcode.subsystems.DriveSubsystem
import teamcode.subsystems.IntakeSubsystem
import teamcode.subsystems.LauncherSubsytem

@TeleOp(name = "Main Op Mode")
class MainOpMode : CommandOpMode() {
    private lateinit var backLeft: Motor
    private lateinit var backRight: Motor
    private lateinit var frontLeft: Motor
    private lateinit var frontRight: Motor

    private lateinit var intakeMotor: MotorEx
    private lateinit var launcherMotor: Motor
    private lateinit var launcherLoader: Motor

    private lateinit var driveGamepad: GamepadEx
    private lateinit var launchGamepad: GamepadEx


    private lateinit var launchButton: GamepadButton
    private lateinit var loadButton: GamepadButton
    private lateinit var intakeButton: GamepadButton
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

        launcherLoader.resetEncoder()
    }

    private fun initGamePads() {
        driveGamepad = GamepadEx(gamepad1)
        launchGamepad = GamepadEx(gamepad2)

        launchButton = GamepadButton(launchGamepad, GamepadKeys.Button.B)
        loadButton = GamepadButton(launchGamepad, GamepadKeys.Button.X)
        intakeButton = GamepadButton(launchGamepad, GamepadKeys.Button.A)
    }

    override fun initialize() {
        initMotors()
        initGamePads()

        val drive = DriveSubsystem(frontLeft, frontRight, backLeft, backRight)
        drive.defaultCommand = DefaultDriveCommand(drive, driveGamepad)

        val intake = IntakeSubsystem(intakeMotor, telemetry)
        val intakeCommand = IntakeCommand(intake, launchGamepad)
        intake.defaultCommand = intakeCommand

        val launcher = LauncherSubsytem(launcherMotor, launcherLoader, telemetry)
        val launchThreeBallsCommand = LaunchThreeBalls(launcher, intake)
        val loadCommand = LoadLauncherCommand(launcher)

        launchButton.whenPressed(launchThreeBallsCommand)
        loadButton.whenReleased(loadCommand)
    }
}