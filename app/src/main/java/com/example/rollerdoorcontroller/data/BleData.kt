package com.example.rollerdoorcontroller.data

enum class ConnectionState {
    Connected,
    Disconnected
}

data class BleData(
    val deviceName: String = "",
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val height: UInt = 0u
)