package teamcode.teleop

import LaunchBallsCommand
import LoadLauncherCommand
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.button.GamepadButton
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.arcrobotics.ftclib.gamepad.GamepadKeys
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import teamcode.commands.DefaultDriveCommand
import teamcode.commands.DefaultLauncherCommand
import teamcode.commands.IntakeCommand
import teamcode.subsystems.DriveSubsystem
import teamcode.subsystems.IntakeSubsystem
import teamcode.subsystems.LauncherSubsystem

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


    private lateinit var longThreeLaunchButton: GamepadButton
    private lateinit var longOneLaunchButton: GamepadButton
    private lateinit var shortThreeLaunchButton: GamepadButton
    private lateinit var shortOneLaunchButton: GamepadButton

    private lateinit var loadButton: GamepadButton

    private lateinit var imu: IMU
    private fun initMotors() {
        backLeft = Motor(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312)
        backRight = Motor(hardwareMap, "backRight", Motor.GoBILDA.RPM_312)
        frontLeft = Motor(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_312)
        frontRight = Motor(hardwareMap, "frontRight", Motor.GoBILDA.RPM_312)

        intakeMotor = MotorEx(hardwareMap, "intakeMotor", Motor.GoBILDA.RPM_312)
        launcherMotor = Motor(hardwareMap, "launcherMotor")
        launcherLoader = Motor(hardwareMap, "launcherLoader", Motor.GoBILDA.RPM_312)


        imu = hardwareMap.get(IMU::class.java, "imu")

        backLeft.inverted = true
        frontLeft.inverted = true
        backRight.inverted = true
        frontRight.inverted = true

        launcherLoader.resetEncoder()
    }

    private fun initGamePads() {
        driveGamepad = GamepadEx(gamepad1)
        launchGamepad = GamepadEx(gamepad2)

        longThreeLaunchButton = GamepadButton(launchGamepad, GamepadKeys.Button.B)
        longOneLaunchButton = GamepadButton(launchGamepad, GamepadKeys.Button.A)
        shortThreeLaunchButton = GamepadButton(launchGamepad, GamepadKeys.Button.Y)
        shortOneLaunchButton = GamepadButton(launchGamepad, GamepadKeys.Button.X)

        loadButton = GamepadButton(launchGamepad, GamepadKeys.Button.DPAD_UP)
    }

    override fun initialize() {
        initMotors()
        initGamePads()

        val drive = DriveSubsystem(frontLeft, frontRight, backLeft, backRight, imu)
        drive.defaultCommand = DefaultDriveCommand(drive, driveGamepad)

        val intake = IntakeSubsystem(intakeMotor, telemetry)

        val intakeCommand = IntakeCommand(intake, launchGamepad)
        intake.defaultCommand = intakeCommand

        val launcher = LauncherSubsystem(launcherMotor, launcherLoader, telemetry)
        val defaultLauncherCommand = DefaultLauncherCommand(launcher, launchGamepad)
        launcher.defaultCommand = defaultLauncherCommand

        val loadCommand = LoadLauncherCommand(launcher, telemetry)

        longThreeLaunchButton.whenPressed(LaunchBallsCommand(launcher, 1.0, 3))
        longOneLaunchButton.whenPressed(LaunchBallsCommand(launcher, 1.0, 1))
        shortThreeLaunchButton.whenPressed(LaunchBallsCommand(launcher, 0.8, 3))
        shortOneLaunchButton.whenPressed(LaunchBallsCommand(launcher, 0.8, 1))

        loadButton.whenPressed(loadCommand)
    }
}