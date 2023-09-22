package com.example.rollerdoorcontroller.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rollerdoorcontroller.ControllerApplication
import com.example.rollerdoorcontroller.data.BleRepository

enum class ControlSignal(val value: Int) {
    STOP(value = 0),
    UP(value = 1),
    DOWN(value = 2)
}

class ControllerViewModel(
    private val bleRepository: BleRepository
) : ViewModel() {
    val bleData = bleRepository.bleData

    fun setControlSignal(signal: ControlSignal) {
        when (signal) {
            ControlSignal.UP -> {
                bleRepository.setControlCharacteristicValue(ControlSignal.UP.value)
            }
            ControlSignal.STOP -> {
                bleRepository.setControlCharacteristicValue(ControlSignal.STOP.value)
            }
            ControlSignal.DOWN -> {
                bleRepository.setControlCharacteristicValue(ControlSignal.DOWN.value)
            }
        }
    }

    // DI
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ControllerApplication)
                ControllerViewModel(bleRepository = application.bleRepository)
            }
        }
    }
}