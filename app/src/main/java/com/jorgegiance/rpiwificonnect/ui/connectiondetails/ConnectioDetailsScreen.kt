package com.jorgegiance.rpiwificonnect.ui.connectiondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jorgegiance.rpiwificonnect.ui.theme.background_grey

@Composable
fun ConnectionDetailsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Margin from all sides
            .clip(RoundedCornerShape(5.dp)) // Rounded corners for the LazyColumn container
            .background(background_grey)
    ){
        Column (

        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp) // Optional padding inside the LazyColumn
            ) {
                items(10) { index ->
                    Button(
                        onClick = { /* Handle button click */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White), // White background for buttons
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp) // Space between buttons
                    ) {
                        Text(text = "Button ${index + 1}", color = Color.Black)
                    }
                }
            }
        }

    }
}

@Preview(device = Devices.PIXEL_6, showBackground = true, showSystemUi = true)
@Composable
fun ConnectionDetailsScreenPreview(modifier: Modifier = Modifier) {
    ConnectionDetailsScreen()
}