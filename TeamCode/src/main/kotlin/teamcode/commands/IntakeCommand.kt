package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.gamepad.GamepadEx
import teamcode.subsystems.IntakeSubsystem

class IntakeCommand(private val intake: IntakeSubsystem, private val gamepadEx: GamepadEx) : CommandBase() {
    init {
        addRequirements(intake)
    }

    override fun execute() {
        intake.rawPowerControl(gamepadEx.leftY)
    }

    override fun end(interrupted: Boolean) {
        intake.stopMotor()
    }
}