package com.example.u_snap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.u_snap.ui.main.USnapMainScreen
import com.example.u_snap.ui.theme.UsnapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UsnapTheme {
                USnapMainScreen()
            }
        }
    }
}