package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.gamepad.GamepadEx
import teamcode.subsystems.DriveSubsystem

class DefaultDriveCommand(private val driveSubsystem: DriveSubsystem, private val gamepad: GamepadEx) : CommandBase() {
    init {
        addRequirements(driveSubsystem)
    }

    override fun execute() {
        val x = gamepad.leftX
        val y = gamepad.leftY
        val rx = gamepad.rightX

        driveSubsystem.drive(x, y, rx)
    }
}