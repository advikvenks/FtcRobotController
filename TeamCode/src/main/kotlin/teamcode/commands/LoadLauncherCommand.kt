package teamcode.commands

import com.arcrobotics.ftclib.command.CommandBase
import com.qualcomm.robotcore.util.ElapsedTime
import teamcode.subsystems.LauncherSubsytem

class LoadLauncherCommand(val launcher: LauncherSubsytem) : CommandBase() {
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
    }

    override fun execute() {
        when (state) {
            LoadState.LOADING -> {
                if (launcher.isLoadingComplete()) {
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
}