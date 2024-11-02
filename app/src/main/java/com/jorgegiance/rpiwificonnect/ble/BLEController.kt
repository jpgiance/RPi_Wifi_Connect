package com.jorgegiance.rpiwificonnect.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.jorgegiance.rpiwificonnect.data.BleDevice
import com.jorgegiance.rpiwificonnect.data.toBleDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BLEController (
    private val context: Context
){
    val TAG = "BLEController"

    private val bluetoothManager by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(BluetoothManager::class.java)
        } else {
            TODO("VERSION.SDK_INT < M")
        }
    }

    private val bluetoothAdapter by lazy {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bluetoothManager?.adapter
        } else {
            BluetoothAdapter.getDefaultAdapter()
        }
    }

    private val bluetoothLeScanner by lazy {
        bluetoothAdapter?.bluetoothLeScanner
    }

    private var _scannedDevices = MutableStateFlow<List<BleDevice>>(emptyList())
    val scannedDevices: StateFlow<List<BleDevice>> get() = _scannedDevices

    private var scanning = false
    private var scanningJob: Job? = null

//    private var deviceConnected: BluetoothDevice

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    @SuppressLint("MissingPermission")
    fun startBleScan(){
        _scannedDevices.value = emptyList()

        scanningJob?.cancel() // Cancel any previous scan

        if(!bluetoothPermissionGranted()) {
            Toast.makeText(context, "Accept Permissions S", Toast.LENGTH_SHORT).show()
            return
        }

        scanningJob = CoroutineScope(Dispatchers.IO).launch {


            bluetoothLeScanner?.startScan(leScanCallback)
            delay(SCAN_PERIOD)
            bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopBleScan(){

        if(!bluetoothPermissionGranted()) {
            Toast.makeText(context, "Accept Permissions S", Toast.LENGTH_SHORT).show()
            return
        }



        bluetoothLeScanner?.stopScan(leScanCallback)
        scanningJob?.cancel()
    }

    private fun bluetoothPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return hasPermission(Manifest.permission.BLUETOOTH_SCAN)

        } else {
            // For older Android versions, check for the location permission
            return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)

        }
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            result.device?.let { device ->

                val newDevice = device.toBleDevice()
                _scannedDevices.update { devices ->
                    if (newDevice in devices) devices else devices + newDevice
                }
            }

        }
    }

    private fun hasPermission(permission: String): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
    }
}