package com.jorgegiance.rpiwificonnect.ui.main

import androidx.lifecycle.ViewModel
import com.jorgegiance.rpiwificonnect.Util.ConnectionState
import com.jorgegiance.rpiwificonnect.ble.BLEController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TopBarViewModel @Inject constructor(
    private var bleController: BLEController
): ViewModel(){

    val bleConnectionState: StateFlow<ConnectionState> = bleController.connectionState

    fun startBleScan(){
        bleController.startBleScan()
    }
}