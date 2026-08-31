package org.firstinspires.ftc.teamcode.subsystems
import com.seattlesolvers.solverslib.command.SubsystemBase
import com.seattlesolvers.solverslib.hardware.motors.Motor
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.max

class LauncherSubsystem(val launcherMotor: Motor, val loadMotor: Motor, val telemetry: Telemetry) : SubsystemBase() {
    private var targetPosition = 0
    private var isLoading = false
    private var isReturning = false


    var max = 0;

    init {
        loadMotor.resetEncoder()
        loadMotor.setRunMode(Motor.RunMode.PositionControl)
        loadMotor.positionCoefficient = 0.04
        loadMotor.setPositionTolerance(10.0)
        loadMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
    }

    fun startLoadingBall(targetAngle: Int) {
        targetPosition = ((targetAngle / 360.0) * loadMotor.cpr).toInt()
        loadMotor.setTargetPosition(targetPosition)
        isLoading = true
        isReturning = false
    }

    fun isLoadingComplete(): Boolean {
        return isLoading && loadMotor.atTargetPosition()
    }

    fun startReturning() {
        loadMotor.setTargetPosition(0)
        isLoading = false
        isReturning = true
    }

    fun isReturningComplete(): Boolean {
        return isReturning && loadMotor.atTargetPosition()
    }

    fun stopLoading() {
        loadMotor.stopMotor()
        isLoading = false
        isReturning = false
    }

    fun speedUpLauncher(power: Double) {
        launcherMotor.set(power)
    }

    fun slowDownLauncher() {
        launcherMotor.stopMotor()
    }

    fun rawPowerControl(power: Double) {
        launcherMotor.set(power)
    }

    override fun periodic() {
        if (isLoading || isReturning) {
            loadMotor.set(0.25)
        }

        telemetry.addData("Load Motor Max Position", maxPos())
        telemetry.addData("Load Motor Target", targetPosition)
        telemetry.addData("Load Motor At Target", loadMotor.atTargetPosition())
        telemetry.addData("Is Loading", isLoading)
        telemetry.addData("Is Returning", isReturning)
        telemetry.update()
    }

    fun maxPos(): Int {
        if (loadMotor.encoder.position > max) {
            max = loadMotor.encoder.position
        }
        return max
    }

    fun resetLoader() {
        loadMotor.stopMotor()
        loadMotor.resetEncoder()
    }

    fun telemetryUpdate() {
        telemetry.addData("Launcher motor", launcherMotor.get())
    }
}
