package com.example.rollerdoorcontroller.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rollerdoorcontroller.R
import com.example.rollerdoorcontroller.data.BleData
import com.example.rollerdoorcontroller.data.ConnectionState

sealed class Screen(val route: String) {
    object Connect: Screen("Connect")
    object Controller: Screen("Controller")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControllerApp() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    // start screen elements
    val connectViewModel: ConnectViewModel = viewModel(factory = ConnectViewModel.Factory)
    val connectionState = connectViewModel.bleData.collectAsState(initial = BleData()).value.connectionState
    val deviceName = connectViewModel.bleData.collectAsState(initial = BleData()).value.deviceName
    val deviceNames = connectViewModel.deviceNames.collectAsState(initial = listOf()).value

    // controller screen elements
    val controllerViewModel: ControllerViewModel = viewModel(factory = ControllerViewModel.Factory)
    val bleData = controllerViewModel.bleData.collectAsState(initial = BleData(connectionState = ConnectionState.Connected)).value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    when (currentDestination?.route) {
                        Screen.Connect.route -> Text("Bonded Devices")
                        Screen.Controller.route -> Text("Controller")
                        else -> Text(text = stringResource(id = R.string.app_name))
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val items = listOf(Screen.Connect, Screen.Controller)

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            when (item) {
                                Screen.Connect -> Icon(Icons.Filled.Home, contentDescription = null)
                                Screen.Controller -> Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            }
                        },
                        label = { Text(item.route) })
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = Screen.Connect.route
        ) {
            composable(Screen.Connect.route) {
                ConnectScreen(
                    deviceNames = deviceNames,
                    connectedDeviceName = deviceName,
                    connectionState = connectionState,
                    connectBle = connectViewModel::connectBle,
                    disconnectBle = connectViewModel::disconnectBle
                )
            }

            composable(Screen.Controller.route) {
                ControllerScreen(
                    bleData = bleData,
                    enableNotification = controllerViewModel::enableNotification,
                    setControlSignal = controllerViewModel::setControlSignal
                )
            }
        }
    }
}