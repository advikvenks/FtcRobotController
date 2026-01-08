import com.arcrobotics.ftclib.command.CommandBase
import com.qualcomm.robotcore.util.ElapsedTime
import teamcode.subsystems.IntakeSubsystem
import teamcode.subsystems.LauncherSubsytem

class LaunchThreeBalls(val launcher: LauncherSubsytem) : CommandBase() {
    private val timer = ElapsedTime()
    private var state = LaunchState.SPIN_UP
    private var ballsLaunched = 0

    enum class LaunchState {
        SPIN_UP,
        LOADING,
        RETURNING,
        WAITING,
        DONE
    }

    init {
        addRequirements(launcher)
    }

    override fun initialize() {
        launcher.resetLoader()
        timer.reset()
        state = LaunchState.SPIN_UP
        ballsLaunched = 0
        launcher.speedUpLauncher()
    }

    override fun execute() {
        when (state) {
            LaunchState.SPIN_UP -> {
                if (timer.seconds() >= 1.0) {
                    launcher.startLoadingBall(75)
                    state = LaunchState.LOADING
                    timer.reset()
                }
            }

            LaunchState.LOADING -> {
                if (launcher.isLoadingComplete()) {
                    launcher.startReturning()
                    state = LaunchState.RETURNING
                    timer.reset()
                }
            }

            LaunchState.RETURNING -> {
                if (launcher.isReturningComplete()) {
                    launcher.resetLoader()
                    ballsLaunched++
                    if (ballsLaunched >= 3) {
                        state = LaunchState.DONE
                        launcher.slowDownLauncher()
                    } else {
                        state = LaunchState.WAITING
                        timer.reset()
                    }
                }
            }

            LaunchState.WAITING -> {
                if (timer.seconds() >= 2.0) {
                    launcher.startLoadingBall(75)
                    state = LaunchState.LOADING
                    timer.reset()
                }
            }

            LaunchState.DONE -> {
                launcher.resetLoader()
            }
        }
    }

    override fun isFinished(): Boolean {
        return state == LaunchState.DONE
    }

    override fun end(interrupted: Boolean) {
        launcher.slowDownLauncher()
        launcher.stopLoading()
    }
}