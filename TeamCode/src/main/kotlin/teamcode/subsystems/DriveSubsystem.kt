package teamcode.subsystems

import android.health.connect.datatypes.units.Power
import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor

class DriveSubsystem(val frontLeft: Motor, val frontRight: Motor, val backLeft: Motor, val backRight: Motor) : SubsystemBase() {
    private val mecanumDrive = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

    private val distancePerPulse = (Math.PI * 104) / backLeft.cpr

    fun drive(x: Double, y: Double, rx: Double) {
        mecanumDrive.driveRobotCentric(x, y, rx)
    }

    fun encoderDrive(bl: Double, br: Double, fl: Double, fr: Double, power: Double) {
        backLeft.setDistancePerPulse(distancePerPulse)
        backRight.setDistancePerPulse(distancePerPulse)
        frontLeft.setDistancePerPulse(distancePerPulse)
        frontRight.setDistancePerPulse(distancePerPulse)

        backLeft.setTargetDistance(bl)
        backLeft.setTargetDistance(br)
        backLeft.setTargetDistance(fl)
        backLeft.setTargetDistance(fr)
    }
}