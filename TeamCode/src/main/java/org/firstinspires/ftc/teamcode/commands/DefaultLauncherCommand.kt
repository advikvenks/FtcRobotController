package org.firstinspires.ftc.teamcode.commands

import com.seattlesolvers.solverslib.command.CommandBase
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import org.firstinspires.ftc.teamcode.subsystems.LauncherSubsystem

class DefaultLauncherCommand(private val launcher: LauncherSubsystem, private val gamepadEx: GamepadEx) : CommandBase() {
    init {
        addRequirements(launcher)
    }

    override fun execute() {
        launcher.rawPowerControl(gamepadEx.leftY)
    }

    override fun end(interrupted: Boolean) {
        launcher.slowDownLauncher()
    }
}
