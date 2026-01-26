package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import teamcode.subsystems.DriveSubsystem

class DriveByDistanceCommand(private val drive: DriveSubsystem, val distance: Double, val moveDeg: Double, val headingDeg: Double, val power: Double, val timeOutS: Double) : CommandBase() {
    init {
        addRequirements(drive)
    }

    override fun initialize() {
        drive.initPosControl()
        drive.encoderDrive(distance, moveDeg, headingDeg, power, timeOutS)
    }

    override fun isFinished(): Boolean {
        return drive.encoderDriveFinished()
    }
}