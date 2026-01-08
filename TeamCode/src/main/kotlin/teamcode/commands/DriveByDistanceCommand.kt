package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import teamcode.subsystems.DriveSubsystem

class DriveByDistanceCommand(private val drive: DriveSubsystem, val bl: Double, val br: Double, val fl: Double, val fr: Double, val power: Double) : CommandBase() {
    init {
        addRequirements(drive)
    }

    override fun initialize() {
        drive.initPosControl()
        drive.encoderDrive(bl, br, fl, fr, power)
    }

    override fun isFinished(): Boolean {
        return drive.encoderDriveFinished()
    }
}