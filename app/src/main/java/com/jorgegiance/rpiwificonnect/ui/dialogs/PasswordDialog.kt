package com.jorgegiance.rpiwificonnect.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jorgegiance.rpiwificonnect.R
import com.jorgegiance.rpiwificonnect.ui.theme.background_grey
import com.jorgegiance.rpiwificonnect.ui.theme.button_orange
import com.jorgegiance.rpiwificonnect.ui.theme.button_purple

@Composable
fun PasswordInputDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    title: String = "Enter Password"
) {
    var password by remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(background_grey)
                        .padding(16.dp)
                ) {
                    Text(
                        text = title,
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold ,
                        fontFamily = FontFamily(Font(R.font.jura_regular)),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = button_orange,  // Orange when focused
                            unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),  // Semi-transparent orange when unfocused
                            focusedLabelColor = Color.Black,  // Orange label when focused
                            unfocusedLabelColor = Color.Black.copy(alpha = 0.5f),  // Semi-transparent orange label when unfocused
                            cursorColor = button_orange,  // Orange cursor
                            focusedTextColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = button_purple
                            )
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                onConfirm(password)
                                password = ""
                            },
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = button_orange, // Orange
                                contentColor = Color.DarkGray)
                        ) {
                            Text("Connect")
                        }
                    }
                }
            }
        }
    }
}


@Preview(device = Devices.PIXEL_6, showBackground = true, showSystemUi = true)
@Composable
fun ConnectionDetailsScreenPreview(modifier: Modifier = Modifier) {
//    Scaffold (
//        topBar = { TopBar() },
//        content = {padding ->
    Box(modifier = Modifier.fillMaxSize()){
        PasswordInputDialog(
            showDialog = true,
            title = "Title",
            onConfirm = {},
            onDismiss = {}
        )
    }


//        }
//    )
}