package org.firstinspires.ftc.teamcode.commands

import com.seattlesolvers.solverslib.command.CommandBase
import com.seattlesolvers.solverslib.gamepad.GamepadEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem

class IntakeCommand(private val intake: IntakeSubsystem, private var speed: Double, private val gamepad: GamepadEx? = null) : CommandBase() {
    private var timer = ElapsedTime()

    init {
        addRequirements(intake)
    }

    override fun execute() {
        if (gamepad == null) {
            intake.rawPowerControl(speed)
        } else {
            intake.rawPowerControl(gamepad.rightY)
        }
    }

    override fun end(interrupted: Boolean) {
        intake.stopMotor()
    }
}
