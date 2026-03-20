package teamcode.commands

import com.seattlesolvers.solverslib.command.CommandBase
import teamcode.subsystems.DriveSubsystem

class DriveByTimeCommand(private val drive: DriveSubsystem, val power: Double, val angle: Double, val time: Double) : CommandBase() {
    init {
        addRequirements(drive)
    }

    override fun initialize() {
        drive.timeDrive(power, angle, time)
    }

    override fun isFinished(): Boolean {
        return (drive.backLeft.get() == 0.0)
    }
}
