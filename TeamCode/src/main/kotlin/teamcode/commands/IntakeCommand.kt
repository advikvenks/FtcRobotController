package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.gamepad.GamepadEx
import com.qualcomm.robotcore.util.ElapsedTime
import teamcode.subsystems.IntakeSubsystem

class IntakeCommand(private val intake: IntakeSubsystem, private val gamepadEx: GamepadEx) : CommandBase() {
    private var timer = ElapsedTime()

    init {
        addRequirements(intake)
    }

    override fun execute() {
        intake.rawPowerControl(gamepadEx.rightY)
    }

    override fun end(interrupted: Boolean) {
        intake.stopMotor()
    }
}