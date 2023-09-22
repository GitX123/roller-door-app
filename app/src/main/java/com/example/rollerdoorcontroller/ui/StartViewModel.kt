package com.example.rollerdoorcontroller.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rollerdoorcontroller.ControllerApplication
import com.example.rollerdoorcontroller.data.BleRepository

class StartViewModel(
    private val bleRepository: BleRepository
) : ViewModel() {
    val bleData = bleRepository.bleData

    fun connectBle() {
        bleRepository.connectBle()
    }

    fun enableNotification() {
        bleRepository.setHeightCharacteristicNotification(true)
    }

    // DI
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ControllerApplication)
                StartViewModel(bleRepository = application.bleRepository)
            }
        }
    }
}