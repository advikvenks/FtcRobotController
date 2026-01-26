import com.arcrobotics.ftclib.command.CommandBase
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import teamcode.subsystems.LauncherSubsystem

class LoadLauncherCommand(val launcher: LauncherSubsystem, val telemetry: Telemetry) : CommandBase() {
    enum class LoadState {
        LOADING,
        RETURNING
    }

    val timer = ElapsedTime()
    var state = LoadState.LOADING

    init {
        addRequirements(launcher)
    }

    override fun initialize() {
        launcher.resetLoader()
        timer.reset()
        state = LoadState.LOADING
        launcher.startLoadingBall(75)
    }

    override fun execute() {
        telemetry.addData("state", state)
        when (state) {
            LoadState.LOADING -> {
                if (launcher.isLoadingComplete()) {
                    telemetry.addData("loading done", state)
                    launcher.startReturning()
                    state = LoadState.RETURNING
                    timer.reset()
                }
            }
            LoadState.RETURNING -> {
                if (launcher.isReturningComplete()) {
                    launcher.resetLoader()
                }
            }
        }
    }

    override fun isFinished(): Boolean {
        state = LoadState.LOADING
        return launcher.isReturningComplete()
    }
}