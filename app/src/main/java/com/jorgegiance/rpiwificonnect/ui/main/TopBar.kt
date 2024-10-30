package com.jorgegiance.rpiwificonnect.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jorgegiance.rpiwificonnect.R
import com.jorgegiance.rpiwificonnect.ui.theme.background_white
import com.jorgegiance.rpiwificonnect.ui.theme.button_orange
import com.jorgegiance.rpiwificonnect.ui.theme.icon_grey
import com.jorgegiance.rpiwificonnect.ui.theme.text_grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { /* Handle button click */ },
                    colors = ButtonDefaults.buttonColors(containerColor = button_orange),
                    shape = RoundedCornerShape(5.dp),
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
                        color = text_grey,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold ,
                        fontFamily = FontFamily(Font(R.font.jura_regular)),

                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "not connected",
                    color = text_grey,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.jura_regular)),
                    modifier = Modifier.padding(end = 1.dp)
                )

                // Right icon
                IconButton(onClick = { /* Handle icon click */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_circle),
                        contentDescription = "Favorite",
                        tint = icon_grey,
                        modifier = Modifier.size(15.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(background_white),
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    TopBar()
}