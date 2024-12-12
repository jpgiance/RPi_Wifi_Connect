package com.jorgegiance.rpiwificonnect.ui.scanlist

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jorgegiance.rpiwificonnect.R
import com.jorgegiance.rpiwificonnect.data.BleDevice
import com.jorgegiance.rpiwificonnect.ui.main.TopBar
import com.jorgegiance.rpiwificonnect.ui.theme.background_grey
import com.jorgegiance.rpiwificonnect.ui.theme.background_white
import com.jorgegiance.rpiwificonnect.ui.theme.button_orange
import com.jorgegiance.rpiwificonnect.ui.theme.icon_grey
import com.jorgegiance.rpiwificonnect.ui.theme.text_grey

@Composable
fun ScanListScreen(
    padding: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: ScanListViewModel = hiltViewModel()
    ) {


    val deviceList by viewModel.bleDevices.collectAsState()

    LaunchedEffect(Unit){
        viewModel.startBleScan()
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(background_grey)
    ){

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 15.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(start = 20.dp, top = 10.dp, bottom = 10.dp, end = 20.dp)
        ) {
            items(deviceList.filter { it.name != "NO NAME" }){ item ->

                LazyItem(
                    device =  item,
                    itemBackgroundColor = background_white,
                    onConnectButtonPressed ={viewModel.connectTo(item.address)}
                )

            }
        }


    }

}

@Composable
fun LazyItem(
    name: String = "",
    device: BleDevice = BleDevice(name = "", address = ""),
    itemBackgroundColor: Color,
    onConnectButtonPressed: ()-> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(5.dp))
            .background(itemBackgroundColor),
        contentAlignment = Alignment.CenterStart

    ) {
        Spacer(modifier = Modifier.size(70.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.padding(5.dp),
                    text = device.name,
                    color = text_grey,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily(Font(R.font.jura_regular)),
                    maxLines = 1,
                    )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = onConnectButtonPressed,
                    colors = ButtonDefaults.buttonColors(containerColor = button_orange),
                    shape = RoundedCornerShape(5.dp),
                    contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = "connect",
                        color = text_grey,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily(Font(R.font.jura_regular)),

                        )
                }
            }

        }


    }
}

@Composable
@Preview(device = Devices.PIXEL_6, showBackground = true, showSystemUi = true)
private fun ScanListScreenPreview() {


}