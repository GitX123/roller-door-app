package com.example.rollerdoorcontroller.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rollerdoorcontroller.data.BleData
import com.example.rollerdoorcontroller.data.ConnectionState

enum class Screen {
    Start,
    Controller
}

@Composable
fun ControllerApp() {
    val navController = rememberNavController()

    // start screen elements
    val startViewModel: StartViewModel = viewModel(factory = StartViewModel.Factory)
    val connectionState = startViewModel.bleData.collectAsState(initial = BleData()).value.connectionState

    // controller screen elements
    val controllerViewModel: ControllerViewModel = viewModel(factory = ControllerViewModel.Factory)
    val bleData = controllerViewModel.bleData.collectAsState(initial = BleData(connectionState = ConnectionState.Connected)).value

    NavHost(
        navController = navController,
        startDestination = Screen.Start.name
    ) {
        composable(Screen.Start.name) {
            StartScreen(
                connectionState = connectionState,
                connectBle = {
                    startViewModel.connectBle()
                },
                navigateToController = {
                    startViewModel.enableNotification()
                    navController.navigate(Screen.Controller.name)
                }
            )
        }

        composable(Screen.Controller.name) {
            ControllerScreen(
                bleData = bleData,
                navigateToStart = { navController.navigateUp() },
                setControlSignal = controllerViewModel::setControlSignal
            )
        }
    }
}