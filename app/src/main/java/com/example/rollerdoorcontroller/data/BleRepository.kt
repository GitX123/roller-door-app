package com.example.rollerdoorcontroller.data

import android.bluetooth.BluetoothDevice

class BleRepository(
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

    fun connectBle(deviceName: String) {
        bleManager.connect(deviceName)
    }

    fun disconnectBle() {
        bleManager.disconnect()
//        bleManager.close()
    }

    fun getBondedDevices(): Set<BluetoothDevice> {
        return bleManager.getBondedDevices()
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