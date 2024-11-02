package com.jorgegiance.rpiwificonnect.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

data class BleDevice (
    val name: String,
    val address: String,
)

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBleDevice(): BleDevice{
    return BleDevice(
        name = name ?: "NO NAME",
        address = address
    )
}

