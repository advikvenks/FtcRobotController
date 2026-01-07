package teamcode.subsystems

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.hardware.motors.Motor
import org.firstinspires.ftc.robotcore.external.Telemetry

class LauncherSubsytem(val launcherMotor: Motor, val loadMotor: Motor, val telemetry: Telemetry) : SubsystemBase() {
    private var targetPosition = 0
    private var isLoading = false
    private var isReturning = false

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

    fun speedUpLauncher() {
        launcherMotor.set(1.0)
    }

    fun slowDownLauncher() {
        launcherMotor.set(0.0)
    }

    override fun periodic() {
        if (isLoading || isReturning) {
            loadMotor.set(0.1)
        }

        telemetry.addData("Load Motor Position", loadMotor.encoder.position)
        telemetry.addData("Load Motor Target", targetPosition)
        telemetry.addData("Load Motor At Target", loadMotor.atTargetPosition())
        telemetry.addData("Is Loading", isLoading)
        telemetry.addData("Is Returning", isReturning)
        telemetry.update()
    }

    fun resetLoader() {
        loadMotor.stopMotor()
        loadMotor.resetEncoder()
    }
}