package BeepTest

import com.fazecast.jSerialComm.SerialPort
import javafx.scene.Parent
import javafx.scene.layout.HBox
import tornadofx.*
import javafx.scene.text.Font
import javafx.stage.Stage
import tornadofx.App
import tornadofx.View

fun main(args: Array<String>) {
    launch<MyApp>(args)
}

class MyApp: App(MyView:: class){
    override fun start(stage: Stage) {
        stage.minHeight = 564.0
        stage.minWidth = 546.0
        stage.isResizable = false
        super.start(stage)
    }
}

class MyView: View() {
    private val controller: MyController by inject()
    private val port = findCommPort(app.parameters.named["port"])
    private val messageLength = byteArrayOf(0x06, 0x00)
    private val command1 = byteArrayOf(0x80.toByte(), 0x03, 0xC8.toByte(), 0x00, 0x64.toByte(), 0x00) // Will beep for 500 ms
    private val command2 = byteArrayOf(0x80.toByte(), 0x03, 0x0B, 0x02, 0x88.toByte(), 0x13) // Will beep for 5000 ms

    init {
        port.openPort()
    }

    override val root = borderpane {
        val birdiePath = "file:///" + app.parameters.named["birdie_path"]

        top = imageview(birdiePath)
        left = generateLeftButton()
        right = generateRightButton()
    }

    private fun findCommPort(portName: String?): SerialPort {
        val commPorts = SerialPort.getCommPorts()

        for (port in commPorts){
            if (port.systemPortName == portName){
                return port
            }
        }

        throw Exception("The communication port named $portName could not be found: Check your bluetooth connection")
    }

    private fun generateLeftButton() : HBox {
        return hbox {
            button("No, thanks.") {
                minWidth = 265.0
                font = Font.font(25.0)

                action {
                    controller.writeToNXT(messageLength, command1, port)
                }
            }
        }
    }

    private fun generateRightButton() :HBox {
        return hbox {
            button("OMFG yes!!!") {
                minWidth = 265.0
                font = Font.font(25.0)
                action {
                    controller.writeToNXT(messageLength, command2, port)
                }
            }
        }
    }
}

class MyController: Controller() {
    fun writeToNXT(messageLength : ByteArray, command : ByteArray, port: SerialPort) {
        port.writeBytes(messageLength, messageLength.size.toLong())
        port.writeBytes(command, command.size.toLong())
    }
}