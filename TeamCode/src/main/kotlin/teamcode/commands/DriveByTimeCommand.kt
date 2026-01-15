package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import teamcode.subsystems.DriveSubsystem

class DriveByTimeCommand(private val drive: DriveSubsystem, val bl: Double, val br: Double, val fl: Double, val fr: Double, val time: Double, val delay: Double = 0.0) : CommandBase() {
    init {
        addRequirements(drive)
    }

    override fun initialize() {
        drive.timeDrive(bl, br, fl, fr, time, delay)
    }

    override fun isFinished(): Boolean {
        return (drive.backLeft.get() == 0.0)
    }
}