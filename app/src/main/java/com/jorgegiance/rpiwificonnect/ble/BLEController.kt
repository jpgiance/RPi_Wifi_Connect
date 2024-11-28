package com.jorgegiance.rpiwificonnect.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.BOND_BONDED
import android.bluetooth.BluetoothDevice.BOND_NONE
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile.STATE_CONNECTED
import android.bluetooth.BluetoothProfile.STATE_CONNECTING
import android.bluetooth.BluetoothProfile.STATE_DISCONNECTED
import android.bluetooth.BluetoothStatusCodes
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.jorgegiance.rpiwificonnect.Util.ConnectionState
import com.jorgegiance.rpiwificonnect.data.BleDevice
import com.jorgegiance.rpiwificonnect.data.toBleDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("MissingPermission")
class BLEController (
    private val context: Context
){
    val TAG = "BLEController"

    private var device: BluetoothDevice? = null
    private var connectedGatt: BluetoothGatt? = null

    private var rxCharacteristic: BluetoothGattCharacteristic? = null
    private var txCharacteristic: BluetoothGattCharacteristic? = null

    private val taskScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val taskQueue = ConcurrentLinkedQueue<() -> Unit>()
    private val taskQueueBusy = AtomicBoolean(false)

    private val notifyingCharacteristics: MutableList<UUID> = mutableListOf()

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

    private var _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> get() = _connectionState

    private var scanningJob: Job? = null

//    private var deviceConnected: BluetoothDevice

    val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true


    fun startBleScan(){

        disconnectDevice()

        _scannedDevices.value = emptyList()

        scanningJob?.cancel() // Cancel any previous scan

        if(!bluetoothPermissionGranted()) {
            Toast.makeText(context, "Accept Permissions S", Toast.LENGTH_SHORT).show()
            return
        }

        scanningJob = CoroutineScope(Dispatchers.IO).launch {


            bluetoothLeScanner?.startScan(leScanCallback)
            delay(Companion.SCAN_PERIOD)
            bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }

    private fun disconnectDevice() {
        device = null
        _connectionState.value = ConnectionState.DISCONNECTED

        connectedGatt?.let { gatt ->
            gatt.disconnect()
            gatt.close()
        }

        connectedGatt = null
        taskQueue.clear()
        taskQueueBusy.set(false)
    }


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

    private val gattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (status == BluetoothGatt.GATT_SUCCESS){

                Log.e(TAG, "onConnectionStateChange: $newState" )

                when (newState) {
                    STATE_CONNECTED -> {
                        _connectionState.value = ConnectionState.CONNECTED


                        device?.let {
                            val bondState = it.bondState
                            Log.e(TAG, "onConnectionStateChange: $bondState" )

                            if (bondState == BOND_BONDED || bondState == BOND_NONE){
                                Log.e(TAG, "onConnectionStateChange: Requesting MTU" )

                                gatt?.let { gatt->

                                    gatt.requestMtu(512)
                                    connectedGatt = gatt
                                }

                            }

                        } ?:{
                            disconnectDevice()
                            Log.e(TAG, "onConnectionStateChange: device is null 22" )
                        }

                    }
                    STATE_DISCONNECTED -> {_connectionState.value = ConnectionState.DISCONNECTED}
                    STATE_CONNECTING -> {_connectionState.value = ConnectionState.CONNECTING}

                    else -> {_connectionState.value = ConnectionState.DISCONNECTED}
                }

            }else{
                Log.e(TAG, "onConnectionStateChange: Random ERROR occurred" )
                Log.e(TAG, "onConnectionStateChange: status: $status" )
                Log.e(TAG, "onConnectionStateChange: new state: $newState" )
                disconnectDevice()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if (status == BluetoothGatt.GATT_FAILURE || gatt == null){
                Log.e(TAG, "onServicesDiscovered: ERROR OCCURRED" )
                disconnectDevice()
                return
            }

            val service: List<BluetoothGattService> = gatt.services

            Log.e(TAG, "onServicesDiscovered: Services discovered: " )

            service.forEach {
                Log.e(TAG, "--- service ID: ${it.uuid} " )

                it.characteristics.forEach { c ->
                    Log.e(TAG, " --------- characteristic Id: ${c.uuid}")

                    c.descriptors.forEach { d ->
                        Log.e(TAG, " --------- ------- descriptor Id: ${d.uuid}")

                    }
                }
            }

            connectedGatt?.let {
                val mService = it.getService(SERVICE_UUID)

                txCharacteristic = mService.getCharacteristic(TX_CHARACTERISTIC_UUID)
                rxCharacteristic = mService.getCharacteristic(RX_CHARACTERISTIC_UUID)

                setNotify(txCharacteristic, true)
            }

            taskQueue.clear()
            taskQueueBusy.set(false)

        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onServiceChanged(gatt: BluetoothGatt) {
            super.onServiceChanged(gatt)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int, value: ByteArray) {
            super.onDescriptorRead(gatt, descriptor, status, value)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)


            // Do some checks first
            descriptor?.let { _descriptor->

                _descriptor.characteristic?.let { parentCharacteristic ->

                    if (status != BluetoothGatt.GATT_SUCCESS) {
                        Log.e( TAG, String.format( "ERROR: Write descriptor failed value , device: %s, characteristic: %s", device?.address, parentCharacteristic.uuid ) )
                    }


                    // Check if this was the Client Configuration Descriptor
                    if (_descriptor.uuid == DESCRIPTOR_UUID) {
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            // Check if we were turning notify on or off
                            val value = _descriptor.value
                            if (value != null) {
                                if (value[0].toInt() != 0) {
                                    // Notify set to on, add it to the set of notifying characteristics
                                    notifyingCharacteristics.add(parentCharacteristic.uuid)
                                } else {
                                    // Notify was turned off, so remove it from the set of notifying characteristics
                                    notifyingCharacteristics.remove(parentCharacteristic.uuid)

                                }
                            }
                        }
                    }
                }

            }


            completedTask()
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
            super.onReliableWriteCompleted(gatt, status)
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)

            Log.e(TAG, "onMtuChanged: mtu = $mtu" )

             if (gatt?.discoverServices() == true){
                 Log.e(TAG, "onMtuChanged: Discovering Services" )
             }else{
                 Log.e(TAG, "onMtuChanged: Discovering Services Failed !!!!!!!!" )
             }

            gatt?.connect()
            device?.createBond()



        }

    }

    fun setNotify(characteristic: BluetoothGattCharacteristic?, enable: Boolean): Boolean {
        if (connectedGatt == null) {
            Log.e(TAG, "ERROR: Gatt is 'null', ignoring set notify request")
            return false
        }

        // Check if characteristic is valid
        if (characteristic == null) {
            Log.e(TAG, "ERROR: Characteristic is 'null', ignoring setNotify request")
            return false
        }

        // Get the CCC Descriptor for the characteristic
        val descriptor = characteristic.getDescriptor(DESCRIPTOR_UUID)
        if (descriptor == null) {
            Log.e(TAG, String.format( "ERROR: Could not get CCC descriptor for characteristic %s",  characteristic.uuid ) )
            return false
        }

        // Check if characteristic has NOTIFY or INDICATE properties and set the correct byte value to be written
        val value: ByteArray
        val properties = characteristic.properties

        value = if ((properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        } else if ((properties and BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
            BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
        } else {
            Log.e( TAG, String.format( "ERROR: Characteristic %s does not have notify or indicate property",  characteristic.uuid ) )
            return false
        }

        val finalValue = if (enable) value else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE


        return enqueueTask {
            Log.e(TAG, "setNotify: notifyRunnable")

            connectedGatt?.let { gatt->

                // First set notification for Gatt object
                if (!gatt.setCharacteristicNotification(descriptor.characteristic, enable)) {
                    Log.e( TAG, String.format( "ERROR: setCharacteristicNotification failed for descriptor: %s", descriptor.uuid ) )
                }

                // Then write to descriptor
                val result: Int?
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result = gatt.writeDescriptor(descriptor, finalValue)

                    if (result != BluetoothStatusCodes.SUCCESS) {
                        Log.e(TAG, "setNotify: result is false")
                        Log.e( TAG, String.format( "ERROR: writeDescriptor failed for descriptor: %s", descriptor.uuid ) )
                        completedTask()
                    } else {
                        Log.e(TAG, "setNotify: result is true")

                    }
                }else{

                    descriptor.setValue(finalValue)
                    val booleanResult = connectedGatt?.writeDescriptor(descriptor)

                    if (booleanResult == null || !booleanResult) {
                        Log.e(TAG, "setNotify: result is false")
                        Log.e( TAG, String.format( "ERROR: writeDescriptor failed for descriptor: %s", descriptor.uuid ) )
                        completedTask()
                    } else {
                        Log.e(TAG, "setNotify: result is true")

                    }
                }

            }?:{
                Log.e(TAG, "ERROR: Gatt is 'null', can not complete read request")
                disconnectDevice()
            }



        }


    }

    fun readCharacteristic(characteristic: BluetoothGattCharacteristic?): Boolean {
        if (connectedGatt == null) {
            Log.e(TAG, "ERROR: Gatt is 'null', ignoring read request")
            return false
        }

        // Check if characteristic is valid
        if (characteristic == null) {
            Log.e(TAG, "ERROR: Characteristic is 'null', ignoring read request")
            return false
        }

        // Check if this characteristic actually has READ property
        if ((characteristic.properties and BluetoothGattCharacteristic.PROPERTY_READ) == 0) {
            Log.e(TAG, "ERROR: Characteristic cannot be read")
            return false
        }


        return enqueueTask {
            connectedGatt?.let { gatt->

                if (!gatt.readCharacteristic(characteristic)) {
                    Log.e( TAG, String.format( "ERROR: readCharacteristic failed for characteristic: %s", characteristic.uuid) )
                    completedTask()
                } else{
                    Log.e(TAG, String.format("reading characteristic <%s>", characteristic.uuid))
                }

            }?:{
                Log.e(TAG, "ERROR: Gatt is 'null', can not complete read request")
                disconnectDevice()
            }

        }


    }

    private fun nextTask() {
        // If there is still a Task being executed then bail out
        if (taskQueueBusy.get()) {
            Log.e(TAG, "nextTask: Queue is busy")
            return
        }

        // Check if we still have a valid gatt object
        if (connectedGatt == null) {
            Log.e( TAG, String.format( "ERROR: GATT is 'null' for peripheral '%s', clearing Task queue", device!!.address ) )
            taskQueue.clear()
            taskQueueBusy.set(false)
            return
        }

        // Execute the next Task in the queue
        if (taskQueue.size > 0) {
            val bluetoothTask = taskQueue.peek()
            taskQueueBusy.set(true)

            bluetoothTask?.let {
                taskScope.launch {
                    try {
                        it()
                    }catch (e: Exception){
                        Log.e( TAG, String.format("ERROR: Task exception for device '%s'", device!!.name), e )
                    }

                }
            }?:{
                completedTask()
            }

        }else{
            taskQueueBusy.set(false)
        }
    }

    private fun completedTask() {
        taskQueue.poll()
        taskQueueBusy.set(false)
        nextTask()
    }

    private fun enqueueTask(task: () -> Unit): Boolean {
        val result = taskQueue.add(task) // Add the task to the queue
        if (taskQueueBusy.get()) {
            nextTask() // Start processing if not busy
        }

        return result
    }

    private fun hasPermission(permission: String): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
    }

    fun connectTo(address: String) {

        stopBleScan()

        device = bluetoothAdapter?.getRemoteDevice(address)

        device?.let {

            if (bluetoothPermissionGranted()){

                it.connectGatt(context, false, gattCallback )
            }else{
                Log.e(TAG, "connectTo: attempted to connect but missing permissions" )
            }

        }?:{
            Log.e(TAG, "connectTo: device not found! Scan again" )
        }



    }

    companion object{
        val SERVICE_UUID: UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
        val RX_CHARACTERISTIC_UUID: UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e")
        val TX_CHARACTERISTIC_UUID: UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")
        val DESCRIPTOR_UUID: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

        // Stops scanning after 10 seconds.
        private val SCAN_PERIOD: Long = 10000
    }
}