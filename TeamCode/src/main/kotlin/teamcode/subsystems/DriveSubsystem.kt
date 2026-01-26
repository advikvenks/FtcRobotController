package teamcode.subsystems

import android.health.connect.datatypes.units.Power
import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.hardware.IMU
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles
import java.lang.Math.toRadians
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class DriveSubsystem(val frontLeft: Motor, val frontRight: Motor, val backLeft: Motor, val backRight: Motor) : SubsystemBase() {
    private val mecanumDrive = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

    private val motors = arrayOf(backLeft, backRight, frontLeft, frontRight)

    private val timer = ElapsedTime()

    private var finished: Boolean = false

    private val headingKp = 0.015

    private lateinit var imu: IMU

    init {
        imu.initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                    RevHubOrientationOnRobot.UsbFacingDirection.UP
                )
            )
        )
    }

    fun drive(x: Double, y: Double, rx: Double) {
//        for (m in motors) {
//            m.setRunMode(Motor.RunMode.RawPower)
//        }
        mecanumDrive.driveRobotCentric(x, y, rx)
    }

    fun encoderDrive(distance: Double, moveDeg: Double, headingDeg: Double, power: Double, timeOutS: Double) {
        timer.reset()

        for (m in motors) {
            m.stopAndResetEncoder()
            m.setRunMode(Motor.RunMode.PositionControl)

            m.setPositionTolerance(13.0)
            m.positionCoefficient = .04
        }



        val y = distance * cos(toRadians(moveDeg))
        val x = distance * sin(toRadians(moveDeg))

        var bl = y - x
        var br = y + x
        var fl = y + x
        var fr = y - x

        val max = maxOf(
            abs(bl), abs(br), abs(fl), abs(fr)
        )

        if (max > 1.0) {
            bl /= max
            br /= max
            fl /= max
            fr /= max
        }

        backLeft.setTargetPosition(tickConversion(bl * distance))
        backRight.setTargetPosition(tickConversion(br * distance))
        frontLeft.setTargetPosition(tickConversion(fl * distance))
        frontRight.setTargetPosition(tickConversion(fr * distance))


        while (motors.any {it.motor.isBusy} && timer.seconds() < timeOutS) {

            val robotOrientation = imu.robotYawPitchRollAngles

            val imuYaw = robotOrientation.getYaw(AngleUnit.DEGREES)

            val headingError = angleWrap(headingDeg - imuYaw)
            val turnCorrection = headingError * headingKp

            backLeft.set(clamp(power * bl + turnCorrection))
            frontLeft.set(clamp(power * fl + turnCorrection))
            backRight.set(clamp(power * br - turnCorrection))
            frontRight.set(clamp(power * fr - turnCorrection))
        }

        motors.forEach { it.stopMotor() }
    }

    fun timeDrive(bl: Double, br: Double, fl: Double, fr: Double, time: Double, delay: Double = 0.0) {
        if (abs(bl) == abs(br) && abs(fl) == abs(fr) && abs(bl) == abs(fl)) {
            timer.reset()

            while (timer.seconds() < time) {
                customPowers(arrayOf(bl, br, fl, fr))
            }

            for (i in 0 until 4) {
                motors[i].stopMotor()
            }

            timer.reset()
            while (timer.seconds() < delay) {

            }
            finished = true
        }
    }

    private fun tickConversion(distance: Double): Int {
        return ((distance / (Math.PI * 4.094)) * frontRight.cpr).toInt()
    }

    private fun customPowers(powers: Array<Double>) {
        for (i in 0 until 4) {
            motors[i].set(powers[i])
        }
    }

    fun initPosControl() {
        for (m in motors) {
            m.setPositionTolerance(10.0)
        }
    }

    fun encoderDriveFinished() : Boolean {
        for (m in motors) {
            if (m.get() != 0.0) {
                return false
            }
        }
        return true
    }

    fun timeDriveFinished() : Boolean {
        return finished
    }

    fun angleWrap(deg: Double) : Double {
        var a = deg
        while (a > 180) a -= 360
        while (a < 180) a += 360
        return a
    }

    fun clamp(power: Double) : Double {
        return power.coerceIn(-1.0, 1.0)
    }
    
    fun getIMUYawPitchRoll() : YawPitchRollAngles {
        return imu.robotYawPitchRollAngles
    }
}