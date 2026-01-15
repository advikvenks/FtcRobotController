package teamcode.subsystems

import android.health.connect.datatypes.units.Power
import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs

class DriveSubsystem(val frontLeft: Motor, val frontRight: Motor, val backLeft: Motor, val backRight: Motor) : SubsystemBase() {
    private val mecanumDrive = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

    private val motors = arrayOf(backLeft, backRight, frontLeft, frontRight)

    private val timer = ElapsedTime()

    private var finished: Boolean = false

    fun drive(x: Double, y: Double, rx: Double) {
        mecanumDrive.driveRobotCentric(x, y, rx)
    }

    fun encoderDrive(bl: Double, br: Double, fl: Double, fr: Double, power: Double) {
        if (abs(bl) == abs(br) && abs(fl) == abs(fr) && abs(bl) == abs(fl)) {
            val targets = arrayOf(bl, br, fl, fr)

            for (i in 0 until 4) {
                motors[i].resetEncoder()
                motors[i].setTargetPosition(tickConversion(targets[i]))
            }

            while (!backRight.atTargetPosition()) {
                for (m in motors) {
                    m.set(power)
                }
            }

            for (i in 0 until 4) {
                motors[i].stopMotor()
            }
        }
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
        return ((distance / (Math.PI * 104)) * frontRight.cpr).toInt()
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
}