package com.example.rollerdoorcontroller.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rollerdoorcontroller.R
import com.example.rollerdoorcontroller.data.BleData
import com.example.rollerdoorcontroller.data.ConnectionState
import com.example.rollerdoorcontroller.ui.theme.RollerDoorControllerTheme

@Composable
fun ControllerScreen(
    bleData: BleData,
    enableNotification: () -> Unit,
    setControlSignal: (ControlSignal) -> Unit,
    modifier: Modifier = Modifier
) {
    if (bleData.connectionState == ConnectionState.Connected) {
        LaunchedEffect(bleData.connectionState) {
            enableNotification()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${bleData.height} cm",
                fontSize = dimensionResource(id = R.dimen.height_info_size).value.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(20.dp))

            // Up button
            IconButton(
                onClick = { setControlSignal(ControlSignal.UP) },
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_button_size)),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_circle_up_24),
                    contentDescription = stringResource(id = R.string.controller_up),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.icon_size))
                )
            }

            // Stop button
            IconButton(
                onClick = { setControlSignal(ControlSignal.STOP) },
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_button_size)),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_stop_circle_24),
                    contentDescription = stringResource(id = R.string.controller_stop),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.icon_size))
                )
            }

            // Down button
            IconButton(
                onClick = { setControlSignal(ControlSignal.DOWN) },
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_button_size))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_circle_down_24),
                    contentDescription = stringResource(id = R.string.controller_down),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.icon_size))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ControllerScreenPreview() {
    RollerDoorControllerTheme {
        ControllerScreen(
            bleData = BleData(height = 200u),
            enableNotification = {},
            setControlSignal = {}
        )
    }
}