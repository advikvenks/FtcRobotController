package teamcode

import com.arcrobotics.ftclib.command.CommandBase
import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.Position

class Launcher(val launcherMotor: MotorEx, val loadMotor: Motor) : SubsystemBase() {

    fun loadBall(targetAngle: Int) {
        var runtime = ElapsedTime()

        if (launcherMotor.get() == 1.0) {
            val targetPosition = ((targetAngle / 360) * 2150.4).toInt()

            loadMotor.setRunMode(Motor.RunMode.PositionControl)

            loadMotor.positionCoefficient = 0.05

            loadMotor.setTargetPosition(targetPosition)
            loadMotor.set(0.0)
            while (!loadMotor.atTargetPosition() && runtime.seconds() < 10.0) {
                val error = targetPosition - loadMotor.encoder.position
                val power = 0.1.coerceAtLeast(0.5.coerceAtMost(0.5 * error / targetPosition))
                loadMotor.set(power)
            }

            loadMotor.stopMotor()

            loadMotor.setTargetPosition(0)
            runtime.reset()

            while (!loadMotor.atTargetPosition() && runtime.seconds() < 10.0) {
                val error = targetPosition - loadMotor.encoder.position
                val power = 0.1.coerceAtLeast(0.5.coerceAtMost(0.5 * error / targetPosition))
                loadMotor.set(power)
            }
            loadMotor.stopMotor()

            runtime.reset()
            while (runtime.seconds() < 1.0) {

            }
        }
    }

    fun speedUpLauncher() {
        launcherMotor.set(1.0)

        var runtime = ElapsedTime()
        while (runtime.seconds() < 1.0) {

        }
    }

    fun slowDownLauncher() {
        launcherMotor.set(0.0)
    }
}

class LaunchThreeBalls(val launcher: Launcher) : CommandBase() {
    init {
        addRequirements(launcher)
    }

    override fun initialize() {
        launcher.speedUpLauncher()

        launcher.loadBall(90)
        launcher.loadBall(90)
        launcher.loadBall(90)
    }

    override fun end(interrupted: Boolean) {
        launcher.slowDownLauncher()
    }
}