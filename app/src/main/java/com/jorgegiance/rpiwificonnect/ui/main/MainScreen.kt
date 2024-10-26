package com.jorgegiance.rpiwificonnect.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jorgegiance.rpiwificonnect.ui.scanlist.ScanListScreen
import com.jorgegiance.rpiwificonnect.ui.theme.RPiWifiConnectTheme
import com.jorgegiance.rpiwificonnect.ui.theme.button_orange
import com.jorgegiance.rpiwificonnect.ui.theme.text_grey

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    Scaffold (
        topBar = { TopBar() },
        content = {padding ->
            MainScreenContent(
                padding = padding
            )

        }
    )
    
}

@Composable
fun MainScreenContent(
    padding: PaddingValues
) {

    ScanListScreen()

}



@Composable
@Preview(device = Devices.PIXEL_6, showBackground = true, showSystemUi = true)
fun MainScreenPreview(modifier: Modifier = Modifier) {
    RPiWifiConnectTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            MainScreen()
        }
    }
}