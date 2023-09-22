package com.example.rollerdoorcontroller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollerdoorcontroller.data.ConnectionState

@Composable
fun StartScreen(
    connectionState: ConnectionState,
    connectBle: () -> Unit,
    navigateToController: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Status: ${connectionState.name}",
                fontSize = 30.sp,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = modifier.size(50.dp))

            ElevatedButton(
                onClick = connectBle,
            ) {
                Text(
                    text = "Connect",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = modifier.size(50.dp))

            Button(
                onClick = navigateToController,
                enabled = (connectionState == ConnectionState.Connected)
            ) {
                Text(
                    text = "Go",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    StartScreen(
        connectionState = ConnectionState.Disconnected,
        connectBle = {},
        navigateToController = {}
    )
}
