package com.example.rollerdoorcontroller.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.abs

private const val TAG = "BleManager"

@SuppressLint("MissingPermission")
class BleManager(
    private val appContext: Context // application context
) {
    private val _bleData =  MutableSharedFlow<BleData>()
    val bleData = _bleData.asSharedFlow()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                coroutineScope.launch {
                    _bleData.emit(BleData(connectionState = ConnectionState.Connected))
                }
                bluetoothGatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                coroutineScope.launch {
                    _bleData.emit(BleData(connectionState = ConnectionState.Disconnected))
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }
        }

        // for API level < 33
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            val value = characteristic.value
            var uintValue: UInt = 0u
            if (value.isNotEmpty()) {
                for (i in value.indices) {
                    uintValue +=  (value[i].toUByte().toUInt() shl i * 8)
                }
                coroutineScope.launch {
                    _bleData.emit(BleData(
                        connectionState = ConnectionState.Connected,
                        height = uintValue
                    ))
                }
            } else {
                Log.w(TAG, "Empty characteristic value")
            }
        }
    }

    // initialize bluetoothAdapter
    fun initialize(): Boolean {
        val bluetoothManager = appContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        if (bluetoothAdapter == null) {
            Log.w(TAG, "Unable to obtain a BluetoothAdapter")
            return false
        }
        Log.i(TAG, "BluetoothAdapter successfully initialized")
        return true
    }

    fun connect(deviceName: String) {
        bluetoothAdapter?.let { adapter ->
            adapter.bondedDevices.find { device ->
                device.name == deviceName
            }?.let { device ->
                bluetoothGatt = device.connectGatt(appContext, false, bluetoothGattCallback)
            } ?: run {
                Log.w(TAG, "Device not found with device name provided")
            }
        } ?: run {
            Log.w(TAG, "BluetoothAdapter not initialized")
        }
    }

    fun close() {
        bluetoothGatt?.let { gatt ->
            gatt.close()
            bluetoothGatt = null
        }
    }

    private fun getCharacteristic(serviceUuid: String, characteristicUuid: String): BluetoothGattCharacteristic? {
        return bluetoothGatt?.let { gatt ->
            gatt.services.find { service ->
                service.uuid == UUID.fromString(serviceUuid)
            }?.characteristics?.find { characteristic ->
                characteristic.uuid == UUID.fromString(characteristicUuid)
            }
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
            null
        }
    }

    fun writeCharacteristic(serviceUuid: String, characteristicUuid: String, value: Int) {
        val characteristic = getCharacteristic(serviceUuid, characteristicUuid)
        characteristic?.let { characteristic ->
            writeCharacteristic(characteristic, value)
        } ?: run {
            Log.w(TAG, "Characteristic specified not found")
        }
    }

    private fun writeCharacteristic(characteristic: BluetoothGattCharacteristic, value: Int) {
        bluetoothGatt?.let { gatt ->
            // for API level < 33
            characteristic.value = byteArrayOf(value.toByte())
            gatt.writeCharacteristic(characteristic)
            // for API level >= 33
            // gatt.writeCharacteristic(characteristic, byteArrayOf(value.toByte()), BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
        }
    }

    fun setCharacteristicNotification(serviceUuid: String, characteristicUuid: String, descriptorUuid: String, enabled: Boolean) {
        val characteristic = getCharacteristic(serviceUuid, characteristicUuid)
        characteristic?.let { characteristic ->
            setCharacteristicNotification(characteristic, descriptorUuid, enabled)
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
        }
    }

    // for API level < 33
    private fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic, descriptorUuid: String, enabled: Boolean) {
        bluetoothGatt?.let { gatt ->
            gatt.setCharacteristicNotification(characteristic, enabled)
            val descriptor = characteristic.getDescriptor(UUID.fromString(descriptorUuid))
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
            // for API level >= 33
            // gatt?.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        } ?: run {
            Log.w(TAG, "BluetoothGatt not initialized")
        }
    }
}