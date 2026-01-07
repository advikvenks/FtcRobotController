package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import teamcode.subsystems.DriveSubsystem

class DriveByDistanceCommand(private val drive: DriveSubsystem) : CommandBase() {
    init {
        addRequirements(drive)
    }

    override fun execute() {

    }
}