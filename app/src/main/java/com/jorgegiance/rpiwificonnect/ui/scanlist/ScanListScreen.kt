package com.jorgegiance.rpiwificonnect.ui.scanlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jorgegiance.rpiwificonnect.R
import com.jorgegiance.rpiwificonnect.ui.theme.background_grey
import com.jorgegiance.rpiwificonnect.ui.theme.background_white
import com.jorgegiance.rpiwificonnect.ui.theme.button_orange
import com.jorgegiance.rpiwificonnect.ui.theme.icon_grey
import com.jorgegiance.rpiwificonnect.ui.theme.text_grey

@Composable
fun ScanListScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(5.dp)) // Rounded corners for the LazyColumn container
            .background(background_grey)
    ){

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(2.dp) // Optional padding inside the LazyColumn
        ) {
            items(10) { index ->
                Box (
                    modifier = Modifier
                        .padding(6.dp) // Margin from all sides
                        .clip(RoundedCornerShape(5.dp)) // Rounded corners for the LazyColumn container
                        .background(background_white)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp), // Add padding to the sides
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Spacer at the start for left alignment of button
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { /* Handle button click */ },
                            colors = ButtonDefaults.buttonColors(containerColor = button_orange),
                            shape = RoundedCornerShape(5.dp), // Set corner radius
                            contentPadding = PaddingValues(start = 8.dp, end = 8.dp),


                            ) {

                            Icon(
                                painter = painterResource(id = R.drawable.icon_bluetooth_searching),
                                contentDescription = "Bluetooth",
                                tint = text_grey // Adjust tint as needed
                            )
                            Spacer(modifier = Modifier.width(5.dp)) // Add spacing between icon and text
                            Text(
                                text = "SCAN",
                                color = text_grey, // Adjust color as needed
                                fontSize = 20.sp, // Set text size to 16sp
                                fontWeight = FontWeight.Bold ,
//                        fontFamily = FontFamily(Font(R.font.jura_regular)), // Set the font to Jura

                            )
                        }

                        Spacer(modifier = Modifier.weight(1f)) // Spacer to push elements to the right

                        // Text label for icon state
                        Text(
                            text = "not connected",
                            color = text_grey,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
//                    fontFamily = FontFamily(Font(R.font.jura_regular)),
                            modifier = Modifier.padding(end = 8.dp) // Adjust padding as needed
                        )

                        // Right icon
                        IconButton(onClick = { /* Handle icon click */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_circle),
                                contentDescription = "Favorite",
                                tint = icon_grey
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp)) // Spacer at the end for right alignment
                    }

                }

            }
        }


    }

}

@Composable
@Preview(device = Devices.PIXEL_6, showBackground = true, showSystemUi = true)
private fun ScanListScreenPreview() {
    ScanListScreen()
}