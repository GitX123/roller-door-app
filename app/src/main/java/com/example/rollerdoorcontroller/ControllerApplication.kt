package com.example.rollerdoorcontroller

import android.app.Application
import com.example.rollerdoorcontroller.data.BleManager
import com.example.rollerdoorcontroller.data.BleRepository

class ControllerApplication : Application() {
    val ServiceUuid = "d973f2e0-b19e-11e2-9e96-0800200c9a66"
    val heightCharacteristicUuid = "d973f2e1-b19e-11e2-9e96-0800200c9a66"
    val heightDescriptorUuid = "00002902-0000-1000-8000-00805F9B34FB"
    val controlCharacteristicUuid = "d973f2e2-b19e-11e2-9e96-0800200c9a66"

    lateinit var bleRepository: BleRepository
    lateinit var bleManager: BleManager

    override fun onCreate() {
        super.onCreate()
        bleManager = BleManager(appContext = applicationContext)
        bleRepository = BleRepository(
            serviceUuid = ServiceUuid,
            heightCharacteristicUuid = heightCharacteristicUuid,
            heightDescriptorUuid = heightDescriptorUuid,
            controlCharacteristicUuid = controlCharacteristicUuid,
            bleManager =bleManager
        )
        bleRepository.initializeBle()
    }
}