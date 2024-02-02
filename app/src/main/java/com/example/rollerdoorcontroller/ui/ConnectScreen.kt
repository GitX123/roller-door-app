package com.example.rollerdoorcontroller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rollerdoorcontroller.data.ConnectionState
import com.example.rollerdoorcontroller.ui.theme.RollerDoorControllerTheme

@Composable
fun ConnectScreen(
    deviceNames: List<String>,
    connectedDeviceName: String,
    connectionState: ConnectionState,
    connectBle: (String) -> Unit,
    disconnectBle: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            deviceNames.forEach { deviceName ->
                BleDeviceCard(
                    deviceName,
                    connectedDeviceName,
                    connectionState,
                    connectBle,
                    disconnectBle
                )
                Spacer(modifier = Modifier.size(10.dp))
            }
        }
    }
}

@Composable
fun BleDeviceCard(
    deviceName: String,
    connectedDeviceName: String,
    connectionState: ConnectionState,
    connectBle: (String) -> Unit,
    disconnectBle: () -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        shape = CardDefaults.elevatedShape
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(3F),
                contentAlignment = Alignment.Center
            ) {
                Text(text = deviceName)
            }
            Box(
                modifier = Modifier.weight(2F),
                contentAlignment = Alignment.Center
            ) {

                if (deviceName == connectedDeviceName && connectionState == ConnectionState.Connected) {
                    Button(
                        onClick = { disconnectBle() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = "Disconnect")
                    }
                } else {
                    Button(
                        onClick = { connectBle(deviceName) },
                    ) {
                        Text(text = "Connect")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectScreenPreview() {
    RollerDoorControllerTheme {
        ConnectScreen(
            deviceNames = listOf("X-BNRG-2", "Arduino"),
            connectedDeviceName = "X-BNRG-2",
            connectionState = ConnectionState.Connected,
            connectBle = {},
            disconnectBle = {}
        )
    }
}

//@Composable
//fun ConnectScreen(
//    connectionState: ConnectionState,
//    connectBle: () -> Unit,
//    navigateToController: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Surface(modifier = modifier.fillMaxSize()) {
//        Column(
//            modifier = modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "Status: ${connectionState.name}",
//                fontSize = 30.sp,
//                fontWeight = FontWeight.Light
//            )
//            Spacer(modifier = modifier.size(50.dp))
//
//            ElevatedButton(
//                onClick = connectBle,
//            ) {
//                Text(
//                    text = "Connect",
//                    fontSize = 30.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//            Spacer(modifier = modifier.size(50.dp))
//
//            Button(
//                onClick = navigateToController,
//                enabled = (connectionState == ConnectionState.Connected)
//            ) {
//                Text(
//                    text = "Go",
//                    fontSize = 30.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun ConnectScreenPreview() {
//    ConnectScreen(
//        connectionState = ConnectionState.Disconnected,
//        connectBle = {},
//        navigateToController = {}
//    )
//}
