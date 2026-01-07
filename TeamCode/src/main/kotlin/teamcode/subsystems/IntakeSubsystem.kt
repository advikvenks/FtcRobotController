package teamcode.subsystems

import com.arcrobotics.ftclib.command.SubsystemBase
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.arcrobotics.ftclib.hardware.motors.MotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry

class IntakeSubsystem(val intakeMotor: MotorEx, val telemetry: Telemetry) : SubsystemBase() {
    private val timer = ElapsedTime()

    init {
        intakeMotor.setRunMode(Motor.RunMode.RawPower)
        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE)
        intakeMotor.resetEncoder()
    }

    fun rawPowerControl(power: Double) {
        intakeMotor.set(power)
    }

    fun stopMotor() {
        intakeMotor.stopMotor()
    }
}