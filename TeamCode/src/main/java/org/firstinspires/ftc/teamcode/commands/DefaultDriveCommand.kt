package org.firstinspires.ftc.teamcode.commands

import com.seattlesolvers.solverslib.command.CommandBase
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem

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
