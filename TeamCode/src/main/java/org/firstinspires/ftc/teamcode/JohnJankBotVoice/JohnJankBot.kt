package teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread

@TeleOp(name = "John Jank AI Bot")
class JohnJankBot : LinearOpMode() {

    // JOHN JANK AI NETWORK VARIABLES
    private val macIp = "192.168.43.219" // UPDATE THIS TO YOUR MAC'S IP
    private val sendPort = 5000
    private val recPort = 5001
    private var tts: AndroidTextToSpeech? = null

    override fun runOpMode() {
        var socket: DatagramSocket? = null

        try {
            // 1. SETUP JOHN JANK AI VOICE & NETWORK
            tts = AndroidTextToSpeech()
            tts?.initialize()
            tts?.setPitch(1.0f)
            tts?.setSpeechRate(1.0f)

            socket = DatagramSocket(recPort)
            val macAddr = InetAddress.getByName(macIp)

            // Background thread to listen for jokes from your Mac and speak them
            thread(start = true) {
                try {
                    val buffer = ByteArray(1024)
                    while (!isStopRequested) {
                        val packet = DatagramPacket(buffer, buffer.size)
                        socket.receive(packet)

                        // Force UTF-8 decoding
                        val joke = String(packet.data, 0, packet.length, Charsets.UTF_8)

                        // Print what the robot received to the Driver Station
                        telemetry.addData("Last Received", joke)
                        telemetry.update()

                        // Speak the joke
                        tts?.speak(joke)
                    }
                } catch (e: Exception) {}
            }

            telemetry.addData("Status", "John Jank AI Network Ready")
            telemetry.update()

            waitForStart()

            val aiTimer = ElapsedTime()

            // 2. Main Teleop Loop For AI triggers
            while (opModeIsActive()) {

                // 5-second cooldown check for John Jank D-Pad triggers on Gamepad 1
                if (aiTimer.seconds() > 5.0) {
                    var roastTarget = ""

                    if (gamepad1.dpad_up) {
                        roastTarget = "COMMAND: Roast the opposing alliance's robot."
                    } else if (gamepad1.dpad_down) {
                        roastTarget = "COMMAND: Roast my own drive team for their driving skills."
                    } else if (gamepad1.dpad_left) {
                        roastTarget = "COMMAND: Roast our high school physics teacher mentor."
                    } else if (gamepad1.dpad_right) {
                        roastTarget = "COMMAND: Kiss up to the FTC judges, but make it sound heavily sarcastic."
                    }

                    if (roastTarget.isNotEmpty()) {
                        val data = roastTarget.toByteArray()
                        thread(start = true) {
                            try {
                                socket.send(DatagramPacket(data, data.size, macAddr, sendPort))
                            } catch (e: Exception) {}
                        }
                        aiTimer.reset()
                    }
                }
            }
        } finally {
            // 3. CLEANUP ON STOP
            socket?.takeIf { !it.isClosed }?.close()
            tts?.close()
        }
    }
}