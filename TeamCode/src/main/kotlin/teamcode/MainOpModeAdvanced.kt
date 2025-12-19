package teamcode

import com.arcrobotics.ftclib.controller.PIDController
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.arcrobotics.ftclib.hardware.motors.Motor
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


    private fun initMotors() {
        backLeft = Motor(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312)
        backRight = Motor(hardwareMap, "backRight", Motor.GoBILDA.RPM_312)
        frontLeft = Motor(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_312)
        frontRight = Motor(hardwareMap, "frontRight", Motor.GoBILDA.RPM_312)

        intakeMotor = Motor(hardwareMap, "intakeMotor", Motor.GoBILDA.RPM_312)
        launcherMotor = Motor(hardwareMap, "launcherMotor")
        launcherLoader = Motor(hardwareMap, "launcherLoader", Motor.GoBILDA.RPM_312)

        launcherMotor.setRunMode(Motor.RunMode.VelocityControl)
        launcherLoader.setRunMode(Motor.RunMode.PositionControl)
        launcherLoader.resetEncoder()

        backLeft.inverted = true
        frontLeft.inverted = true
    }

    override fun runOpMode() {

        initMotors()

        val mecanum = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

        waitForStart()

        while (opModeIsActive()) {
            val x = gamepad1.left_stick_x.toDouble()
            val y = -gamepad1.left_stick_y.toDouble()
            val rx = gamepad1.right_stick_x.toDouble()

            mecanum.driveRobotCentric(x, y, rx)
        }
    }
}