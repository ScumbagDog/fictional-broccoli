package BeepTest

import com.fazecast.jSerialComm.SerialPort

fun main(args: Array<String>) {
    val bluetoothConnection = findCommPort(args)
    val messageLength = byteArrayOf(0x06, 0x00)
    val command = byteArrayOf(0x80.toByte(), 0x03, 0x0B, 0x02, 0xF4.toByte(), 0x01)

    bluetoothConnection.openPort()
    bluetoothConnection.writeBytes(messageLength, messageLength.size.toLong())
    bluetoothConnection.writeBytes(command, command.size.toLong())
    //bluetoothConnection.closePort()
}

fun findCommPort(args: Array<String>): SerialPort {
    val portName = findPortName(args)
    val commPorts = SerialPort.getCommPorts()

    for (port in commPorts){
        if (port.systemPortName == portName){
            return port
        }
    }

    throw Exception("The communication port named $portName could not be found: Check your bluetooth connection")
}

fun findPortName(args: Array<String>) : String{
    if (args.isEmpty()) {
        throw Exception("Port name is missing: Add port name to program arguments")
    }
    return args[0]
}
