package com.example.u_snap.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.u_snap.ui.camera.CameraScreen
import com.example.u_snap.ui.components.USnapFloatingNavBar

private const val HOME_INDEX = 0
private const val MAPS_INDEX = 1
private const val CAMERA_INDEX = 2
private const val CHAT_INDEX = 3

@Composable
fun USnapMainScreen(
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedIndex by rememberSaveable { mutableIntStateOf(CAMERA_INDEX) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        when (selectedIndex) {
            HOME_INDEX -> PlaceholderSection(
                title = "Home",
                subtitle = "Feed, highlights, and your latest snaps."
            )
            MAPS_INDEX -> PlaceholderSection(
                title = "Maps",
                subtitle = "Location-based moments will live here."
            )
            CAMERA_INDEX -> CameraScreen(
                lifecycleOwner = lifecycleOwner,
                modifier = Modifier.fillMaxSize()
            )
            CHAT_INDEX -> PlaceholderSection(
                title = "Chat",
                subtitle = "Conversation threads and quick replies."
            )
        }

        USnapFloatingNavBar(
            selectedIndex = selectedIndex,
            onSelectedIndexChange = { selectedIndex = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 24.dp)
        )
    }
}

@Composable
private fun PlaceholderSection(
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB0B0B0),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}