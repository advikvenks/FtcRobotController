package teamcode.subsystems

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.drivebase.MecanumDrive
import com.arcrobotics.ftclib.hardware.motors.Motor

class DriveSubsystem(frontLeft: Motor, frontRight: Motor, backLeft: Motor, backRight: Motor) : SubsystemBase() {
    private val mecanumDrive = MecanumDrive(frontLeft, frontRight, backLeft, backRight)

    fun drive(x: Double, y: Double, rx: Double) {
        mecanumDrive.driveRobotCentric(x, y, rx)
    }
}