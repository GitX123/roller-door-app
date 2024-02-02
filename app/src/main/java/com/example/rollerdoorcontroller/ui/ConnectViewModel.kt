package com.example.rollerdoorcontroller.ui

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.rollerdoorcontroller.ControllerApplication
import com.example.rollerdoorcontroller.data.BleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
class ConnectViewModel(
    private val bleRepository: BleRepository
) : ViewModel() {
    val bleData = bleRepository.bleData
    val _deviceNames = MutableSharedFlow<List<String>>()
    var deviceNames = _deviceNames.asSharedFlow()

    init {

        viewModelScope.launch {
            while (true) {
                val bondedDeviceNames = bleRepository.getBondedDevices().map { it.name }
                if (deviceNames != bondedDeviceNames) {
                    _deviceNames.emit(bondedDeviceNames)
                }
                delay(100)
            }
        }
    }

    fun connectBle(deviceName: String) {
        bleRepository.connectBle(deviceName)
    }

    fun disconnectBle() {
        bleRepository.disconnectBle()
    }

    // DI
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ControllerApplication)
                ConnectViewModel(bleRepository = application.bleRepository)
            }
        }
    }
}