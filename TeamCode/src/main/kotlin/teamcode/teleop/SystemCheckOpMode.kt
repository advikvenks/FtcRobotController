package teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.max

@Suppress("unused")
@TeleOp(name = "System Check OpMode", group = "Linear OpMode")
class SystemCheckOpMode : LinearOpMode() {
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

    private fun controlIntake() {
        if (gamepad2.cross) {
            intakeMotor.power = 0.4
        } else {
            intakeMotor.power = -gamepad2.left_stick_y.toDouble()
            telemetry.addData("Intake power:", "%4.2f", intakeMotor.power)
        }
    }

    private fun controlLauncher() {
        if (gamepad2.triangle) {
            launcherMotor.power = 1.0
        } else {
            launcherMotor.power = 0.0
        }
    }

    private fun launcherFlick() {
    }

    private fun smartSleep(seconds: Double) {
        val timer = ElapsedTime()
        while (timer.seconds() < seconds && opModeIsActive()) {

        }
    }

    override fun runOpMode() {
        var loadPower = 0.0


        initDevices() //initialize the motors and servos and stuff
        telemetry.addData("Status", "Initialized")
        telemetry.update()

        waitForStart()
        runtime.reset()

        //This is the main control loop and basically is a loop that runs until we end the opmode
        while (opModeIsActive()) {
            controlIntake()
            controlLauncher()

            if (gamepad1.x) {
                backLeftDrive.power = 1.0
            }
            if (gamepad1.y) {
                backRightDrive.power = 1.0
            }
            if (gamepad1.b) {
                frontLeftDrive.power = 1.0
            }
            if (gamepad1.a) {
                frontRightDrive.power = 1.0
            }

            if (gamepad1.dpadUpWasPressed()) {
                loadPower += .05
            }

            if (gamepad1.dpadDownWasPressed()) {
                loadPower -= .05
            }

            telemetry.addData("power: ", loadPower)

            telemetry.update()
        }
    }
}