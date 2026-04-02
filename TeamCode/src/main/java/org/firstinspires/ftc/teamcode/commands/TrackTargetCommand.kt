package org.firstinspires.ftc.teamcode.commands

import com.seattlesolvers.solverslib.command.CommandBase
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem

class TrackTargetCommand(
    private val drive: DriveSubsystem,
    private val limelight: LimelightSubsystem
) : CommandBase() {

    private val kP = 0.03

    init {
        addRequirements(drive)
    }

    override fun execute() {
        if (limelight.hasTarget()) {
            val error = limelight.getHorizontalOffset()
            val turnPower = error * kP

            drive.drive(0.0, 0.0, turnPower)
        } else {
            drive.drive(0.0, 0.0, 0.0)
        }
    }

    override fun end(interrupted: Boolean) {
        drive.drive(0.0, 0.0, 0.0)
    }
}