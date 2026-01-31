package teamcode.autonomous

import LaunchBallsCommand
import com.arcrobotics.ftclib.command.CommandOpMode
import com.arcrobotics.ftclib.command.SequentialCommandGroup
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.IMU
import teamcode.commands.DriveByTimeCommand
import teamcode.subsystems.DriveSubsystem
import teamcode.subsystems.IntakeSubsystem
import teamcode.subsystems.LauncherSubsystem

@Autonomous(name = "Long Auto")
class LongAuto : CommandOpMode() {
    private lateinit var backLeft: Motor
    private lateinit var backRight: Motor
    private lateinit var frontLeft: Motor
    private lateinit var frontRight: Motor
    private lateinit var launcherMotor: Motor
    private lateinit var launcherLoader: Motor
    private lateinit var intakeMotor: MotorEx

    private lateinit var imu: IMU
    private fun initMotors() {
        backLeft = Motor(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312)
        backRight = Motor(hardwareMap, "backRight", Motor.GoBILDA.RPM_312)
        frontLeft = Motor(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_312)
        frontRight = Motor(hardwareMap, "frontRight", Motor.GoBILDA.RPM_312)

        launcherMotor = Motor(hardwareMap, "launcherMotor")
        launcherLoader = Motor(hardwareMap, "launcherLoader", Motor.GoBILDA.RPM_312)

        intakeMotor = MotorEx(hardwareMap, "intakeMotor", Motor.GoBILDA.RPM_312)

        imu = hardwareMap.get(IMU::class.java, "imu")

        backLeft.inverted = true
        frontLeft.inverted = true

        launcherLoader.resetEncoder()
    }

    override fun initialize() {
        initMotors()

        val drive = DriveSubsystem(frontLeft, frontRight, backLeft, backRight, imu)

        val launcher = LauncherSubsystem(launcherMotor, launcherLoader, telemetry)
        val launchBallsCommand = LaunchBallsCommand(launcher, 1.0, 3)

        val intake = IntakeSubsystem(intakeMotor, telemetry)

        val power = 0.4

        waitForStart()

        if (opModeIsActive()) {
            schedule(
                SequentialCommandGroup (
                    launchBallsCommand,
                    DriveByTimeCommand(drive, power, power, power, power, 1.0),
                )
            )
        }
    }
}