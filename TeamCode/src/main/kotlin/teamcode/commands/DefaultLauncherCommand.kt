package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.gamepad.GamepadEx
import teamcode.subsystems.LauncherSubsystem

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