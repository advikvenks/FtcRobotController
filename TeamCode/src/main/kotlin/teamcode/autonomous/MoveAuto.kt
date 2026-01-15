package teamcode.autonomous

import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

@Autonomous
class MoveAuto : LinearOpMode() {
    private lateinit var backLeft: Motor
    private lateinit var backRight: Motor
    private lateinit var frontLeft: Motor
    private lateinit var frontRight: Motor

    override fun runOpMode() {
        backLeft = Motor(hardwareMap, "backLeft", Motor.GoBILDA.RPM_312)
        backRight = Motor(hardwareMap, "backRight", Motor.GoBILDA.RPM_312)
        frontLeft = Motor(hardwareMap, "frontLeft", Motor.GoBILDA.RPM_312)
        frontRight = Motor(hardwareMap, "frontRight", Motor.GoBILDA.RPM_312)

        backLeft.inverted = true
        frontLeft.inverted = true

        waitForStart()

        backLeft.set(0.2)
        frontLeft.set(0.2)
        backRight.set(0.2)
        frontRight.set(0.2)

        sleep(1000)

        backLeft.stopMotor()
        frontLeft.stopMotor()
        backRight.stopMotor()
        frontRight.stopMotor()
    }


}