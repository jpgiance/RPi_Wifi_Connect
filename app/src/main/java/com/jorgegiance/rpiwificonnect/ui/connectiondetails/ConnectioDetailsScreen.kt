package com.jorgegiance.rpiwificonnect.ui.connectiondetails

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import com.jorgegiance.rpiwificonnect.R
import com.jorgegiance.rpiwificonnect.ui.dialogs.PasswordInputDialog
import com.jorgegiance.rpiwificonnect.ui.main.TopBar
import com.jorgegiance.rpiwificonnect.ui.scanlist.LazyItem
import com.jorgegiance.rpiwificonnect.ui.scanlist.ScanListScreen
import com.jorgegiance.rpiwificonnect.ui.theme.background_grey
import com.jorgegiance.rpiwificonnect.ui.theme.background_white
import com.jorgegiance.rpiwificonnect.ui.theme.icon_green
import com.jorgegiance.rpiwificonnect.ui.theme.icon_grey
import com.jorgegiance.rpiwificonnect.ui.theme.text_grey

@Composable
fun ConnectionDetailsScreen(padding: PaddingValues, modifier: Modifier = Modifier) {



    var openAlertDialog = remember { mutableStateOf(false) }


    PasswordInputDialog(
        showDialog = openAlertDialog.value,
        onDismiss = {
            openAlertDialog.value = false
        },
        onConfirm = { password ->
            openAlertDialog.value = false
//            onPasswordConfirmed(password)
            Log.e("TAG", "ConnectionDetailsScreen Password is: $password", )
        },
        title = "Enter Wifi Password"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(background_grey)
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp)
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(background_white)
        ){
            Column (

            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    ){
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "Wifi Setup",
                            color = text_grey,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black ,
                            fontFamily = FontFamily(Font(R.font.jura_regular)),
                        )

                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End

                    ){
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = "Ip: 50.166.38.81",
                            color = text_grey,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.jura_regular)),
                        )
                        IconButton(onClick = { /* Handle icon click */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_circle),
                                contentDescription = "Favorite",
                                tint = icon_green,
                                modifier = Modifier.size(15.dp)
                            )
                        }

                    }

                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(start = 20.dp, top = 10.dp, bottom = 10.dp, end = 20.dp)
                ) {
                    items(10) { index ->
                        LazyItem(
                            name = "Wifi $index",
                            itemBackgroundColor = background_grey,
                            onConnectButtonPressed = {openAlertDialog.value = true}
                        )
                    }
                }
            }
        }


    }
}

@Preview(device = Devices.PIXEL_6, showBackground = true, showSystemUi = true)
@Composable
fun ConnectionDetailsScreenPreview(modifier: Modifier = Modifier) {
    Scaffold (
        topBar = { TopBar(onScanPressed = {}) },
        content = {padding ->
            ConnectionDetailsScreen(
                padding = padding
            )

        }
    )
}