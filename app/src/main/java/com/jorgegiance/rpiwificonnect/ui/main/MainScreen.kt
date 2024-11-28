package com.jorgegiance.rpiwificonnect.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgegiance.rpiwificonnect.Util.ConnectionState
import com.jorgegiance.rpiwificonnect.ui.connectiondetails.ConnectionDetailsScreen
import com.jorgegiance.rpiwificonnect.ui.scanlist.ScanListScreen
import com.jorgegiance.rpiwificonnect.ui.theme.RPiWifiConnectTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel(),
) {

    val connectionState by viewModel.bleConnectionState.collectAsState(ConnectionState.DISCONNECTED)

    Scaffold (
        topBar = {  TopBar(viewModel = hiltViewModel()) },
        content = {padding ->

            if (connectionState == ConnectionState.CONNECTED){
                ConnectionDetailsScreen( padding = padding )
            }else{
                ScanListScreen( padding = padding )
            }

        }
    )
    
}





@Composable
@Preview(device = Devices.PIXEL_6, showBackground = true, showSystemUi = true)
fun MainScreenPreview(modifier: Modifier = Modifier) {

}