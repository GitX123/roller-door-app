package com.example.rollerdoorcontroller

import android.app.Application
import com.example.rollerdoorcontroller.data.BleManager
import com.example.rollerdoorcontroller.data.BleRepository

class ControllerApplication : Application() {
    val DeviceName = "Arduino"
    val ServiceUuid = "00000020-0000-1000-8000-00805f9b34fb"
    val heightCharacteristicUuid = "00000021-0000-1000-8000-00805f9b34fb"
    val heightDescriptorUuid = "00002902-0000-1000-8000-00805F9B34FB"
    val controlCharacteristicUuid = "00000022-0000-1000-8000-00805f9b34fb"

    lateinit var bleRepository: BleRepository
    lateinit var bleManager: BleManager

    override fun onCreate() {
        super.onCreate()
        bleManager = BleManager(appContext = applicationContext)
        bleRepository = BleRepository(
            deviceName = DeviceName,
            serviceUuid = ServiceUuid,
            heightCharacteristicUuid = heightCharacteristicUuid,
            heightDescriptorUuid = heightDescriptorUuid,
            controlCharacteristicUuid = controlCharacteristicUuid,
            bleManager =bleManager
        )
        bleRepository.initializeBle()
    }
}