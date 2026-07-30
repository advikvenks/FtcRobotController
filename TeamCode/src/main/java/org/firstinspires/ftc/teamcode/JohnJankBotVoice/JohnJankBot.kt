package teamcode.teleop

import com.qualcomm.hardware.limelightvision.Limelight3A
import org.firstinspires.ftc.teamcode.commands.LaunchBallsCommand
import org.firstinspires.ftc.teamcode.commands.LoadLauncherCommand
import com.seattlesolvers.solverslib.command.CommandOpMode
import com.seattlesolvers.solverslib.command.button.GamepadButton
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.seattlesolvers.solverslib.gamepad.GamepadKeys
import com.seattlesolvers.solverslib.hardware.motors.Motor
import com.seattlesolvers.solverslib.hardware.motors.MotorEx
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.commands.DefaultDriveCommand
import org.firstinspires.ftc.teamcode.commands.DefaultLauncherCommand
import org.firstinspires.ftc.teamcode.commands.IntakeCommand
import org.firstinspires.ftc.teamcode.commands.TrackTargetCommand
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem

// JOHN JANK AI IMPORTS
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread
import com.qualcomm.robotcore.util.ElapsedTime

@TeleOp(name = "John Jank AI Bot")
class JohnJankBot : CommandOpMode() {

    //  JOHN JANK AI NETWORK VARIABLES
    private val macIp = "192.168.43.219" // UPDATE THIS TO YOUR MAC'S IP
    private val sendPort = 5000
    private val recPort = 5001
    private var tts: AndroidTextToSpeech? = null
    private var socket: DatagramSocket? = null
    private lateinit var macAddr: InetAddress
    private val aiTimer = ElapsedTime()

    //  YOUR EXISTING SUBSYSTEM & MOTOR VARIABLES
    private lateinit var limelightSubsystem: LimelightSubsystem
    private lateinit var limelight: Limelight3A

    private lateinit var trackButton: GamepadButton

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

        limelight = hardwareMap.get(Limelight3A::class.java, "limelight")
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

        trackButton = GamepadButton(driveGamepad, GamepadKeys.Button.LEFT_BUMPER)
        loadButton = GamepadButton(launchGamepad, GamepadKeys.Button.DPAD_UP)
    }

    override fun initialize() {
        //  1. SETUP JOHN JANK AI VOICE & NETWORK
        try {
            tts = AndroidTextToSpeech()
            tts?.initialize()
            tts?.setPitch(1.0f)
            tts?.setSpeechRate(1.0f)

            socket = DatagramSocket(recPort)
            macAddr = InetAddress.getByName(macIp)

            // Background thread to listen for jokes from your Mac and speak them
            thread(start = true) {
                try {
                    val buffer = ByteArray(1024)
                    while (!isStopRequested) {
                        val packet = DatagramPacket(buffer, buffer.size)
                        socket?.receive(packet)

                        // 1. Force UTF-8 decoding
                        val joke = String(packet.data, 0, packet.length, Charsets.UTF_8)

                        // 2. Print what the robot received to the Driver Station
                        telemetry.addData("Last Received", joke)
                        telemetry.update()

                        // 3. Speak the joke
                        tts?.speak(joke)
                    }
                } catch (e: Exception) {}
            }
        } catch (e: Exception) {
            telemetry.addData("AI Error", "Could not start John Jank network")
        }

        //  2. SETUP YOUR MOTORS & SUBSYSTEMS
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

        longThreeLaunchButton.whenPressed(LaunchBallsCommand(launcher, 1.0, 3, 2.0))
        longOneLaunchButton.whenPressed(LaunchBallsCommand(launcher, 1.0, 1, 2.0))
        shortThreeLaunchButton.whenPressed(LaunchBallsCommand(launcher, 0.8, 3, 2.0))
        shortOneLaunchButton.whenPressed(LaunchBallsCommand(launcher, 0.8, 1, 2.0))

        loadButton.whenPressed(loadCommand)

        limelightSubsystem = LimelightSubsystem(limelight, telemetry)
        val trackCommand = TrackTargetCommand(drive, limelightSubsystem)
        trackButton.whileHeld(trackCommand)

        aiTimer.reset()
    }

    //  3. RUN LOOP FOR COMMAND SCHEDULER & AI TRIGGERS
    override fun run() {
        // MUST call super.run() so SolversLib executes your commands!
        super.run()

        // 5-second cooldown check for John Jank D-Pad triggers on Gamepad 1
        if (aiTimer.seconds() > 5.0) {
            var roastTarget = ""

            if (gamepad1.dpad_up) {
                roastTarget = "COMMAND: Roast the opposing alliance's robot."
            } else if (gamepad1.dpad_down) {
                roastTarget = "COMMAND: Roast my own drive team for their driving skills."
            } else if (gamepad1.dpad_left) {
                roastTarget = "COMMAND: Roast our high school physics teacher mentor."
            } else if (gamepad1.dpad_right) {
                roastTarget = "COMMAND: Kiss up to the FTC judges, but make it sound heavily sarcastic."
            }

            if (roastTarget.isNotEmpty()) {
                val data = roastTarget.toByteArray()
                thread(start = true) {
                    try {
                        socket?.send(DatagramPacket(data, data.size, macAddr, sendPort))
                    } catch (e: Exception) {}
                }
                aiTimer.reset()
            }
        }
    }

    //  4. CLEANUP ON STOP
    override fun reset() {
        super.reset()
        socket?.takeIf { !it.isClosed }?.close()
        tts?.close()
    }
}