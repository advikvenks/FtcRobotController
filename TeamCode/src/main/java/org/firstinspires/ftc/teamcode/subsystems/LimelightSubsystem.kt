package org.firstinspires.ftc.teamcode.subsystems

import com.qualcomm.hardware.limelightvision.LLResult
import com.qualcomm.hardware.limelightvision.Limelight3A
import com.seattlesolvers.solverslib.command.SubsystemBase
import org.firstinspires.ftc.robotcore.external.Telemetry

class LimelightSubsystem(val limelight: Limelight3A, val telemetry: Telemetry) : SubsystemBase() {

    init {
        limelight.setPollRateHz(100)
        limelight.pipelineSwitch(1)
        limelight.start()
    }

    fun hasTarget(): Boolean {
        val res = limelight.latestResult
        return res != null && res.isValid
    }

    fun getHorizontalOffset(): Double {
        return limelight.latestResult?.tx ?: 0.0
    }

    override fun periodic() {
        val result = limelight.latestResult

        if (result != null && result.isValid) {
            telemetry.addData("Limelight", "Target Found")
            telemetry.addData("Pipeline", result.pipelineIndex)
            telemetry.addData("TX", result.tx)
            telemetry.addData("TY", result.ty)
        } else {
            // This tells you if the code thinks it's still in Pipe 1
            telemetry.addData("Limelight", "Searching (Pipe 1)...")
        }
    }

    fun stop() {
        limelight.stop()
    }
}