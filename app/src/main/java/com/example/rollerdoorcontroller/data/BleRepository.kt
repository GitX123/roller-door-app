package com.example.rollerdoorcontroller.data

class BleRepository(
    private val deviceName: String,
    private val serviceUuid: String,
    private val heightCharacteristicUuid: String,
    private val heightDescriptorUuid: String,
    private val controlCharacteristicUuid: String,
    private val bleManager: BleManager
) {
    val bleData = bleManager.bleData

    fun initializeBle() {
        bleManager.initialize()
    }

    fun connectBle() {
        bleManager.connect(deviceName)
    }

    fun setHeightCharacteristicNotification(enable: Boolean) {
        bleManager.setCharacteristicNotification(
            serviceUuid = serviceUuid,
            characteristicUuid = heightCharacteristicUuid,
            descriptorUuid = heightDescriptorUuid,
            enabled = enable
        )
    }

    fun setControlCharacteristicValue(value: Int) {
        bleManager.writeCharacteristic(
            serviceUuid = serviceUuid,
            characteristicUuid = controlCharacteristicUuid,
            value = value
        )
    }
}