package BeepTest

import com.fazecast.jSerialComm.SerialPort
import javafx.*
import tornadofx.*

fun main() {
    launch<MyApp>()

    val COM4 = SerialPort.getCommPorts()[0]
    val messageLength = byteArrayOf(0x06, 0x00)
    val command = byteArrayOf(0x80.toByte(), 0x03, 0x0B, 0x02, 0xF4.toByte(), 0x01)

    COM4.openPort()
    COM4.writeBytes(messageLength, messageLength.size.toLong())
    COM4.writeBytes(command, command.size.toLong())
    COM4.closePort()

}
class MyApp: App(MyView::class)
class MyView: View() {
    override val root = vbox {
        button("Press me")
        label("Waiting")
    }
}
