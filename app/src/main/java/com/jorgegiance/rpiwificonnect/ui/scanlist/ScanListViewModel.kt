package com.jorgegiance.rpiwificonnect.ui.scanlist

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import com.jorgegiance.rpiwificonnect.ble.BLEController
import com.jorgegiance.rpiwificonnect.data.BleDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScanListViewModel @Inject constructor(
    private var bleController: BLEController
):ViewModel() {

    val bleDevices: StateFlow<List<BleDevice>> = bleController.scannedDevices


    fun startBleScan(){
        bleController.startBleScan()
    }

    fun stopBleScan(){
        bleController.stopBleScan()
    }
}