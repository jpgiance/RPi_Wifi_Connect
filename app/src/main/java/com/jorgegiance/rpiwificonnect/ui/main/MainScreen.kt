package com.jorgegiance.rpiwificonnect.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.jorgegiance.rpiwificonnect.ui.connectiondetails.ConnectionDetailsScreen
import com.jorgegiance.rpiwificonnect.ui.scanlist.ScanListScreen
import com.jorgegiance.rpiwificonnect.ui.theme.RPiWifiConnectTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onScanPressed: ()->Unit
) {

    Scaffold (
        topBar = { TopBar(onScanPressed = onScanPressed) },
        content = {padding ->
//            ConnectionDetailsScreen( padding = padding )
            ScanListScreen( padding = padding )

        }
    )
    
}





@Composable
@Preview(device = Devices.PIXEL_6, showBackground = true, showSystemUi = true)
fun MainScreenPreview(modifier: Modifier = Modifier) {
    RPiWifiConnectTheme {
        Scaffold (
            topBar = { TopBar(onScanPressed = {}) },
            content = {padding ->
                ScanListScreen(
                    padding = padding
                )

            }
        )
    }
}