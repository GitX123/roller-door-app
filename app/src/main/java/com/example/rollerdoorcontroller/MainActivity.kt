package com.example.rollerdoorcontroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.rollerdoorcontroller.ui.ControllerApp
import com.example.rollerdoorcontroller.ui.theme.RollerDoorControllerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RollerDoorControllerTheme{
                ControllerApp()
            }
        }
    }
}