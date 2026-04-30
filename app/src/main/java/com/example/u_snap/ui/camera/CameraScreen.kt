package com.example.u_snap.ui.camera

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

/**
 * Main camera screen composable that orchestrates permission handling and camera preview.
 * 
 * This composable:
 * 1. Checks if camera permission is already granted
 * 2. If not granted, shows permission request screen
 * 3. If granted, shows full-screen camera preview
 * 
 * @param lifecycleOwner The lifecycle owner (typically the Activity)
 * @param modifier Modifier for layout customization
 */
@Composable
fun CameraScreen(
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    var cameraPermissionGranted by remember {
        val context = lifecycleOwner as android.content.Context
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    var permissionDenied by remember { mutableStateOf(false) }
    
    when {
        cameraPermissionGranted -> {
            // Show camera preview with MediaPipe analyzer and Canvas overlay
            CameraWithAnalyzerScreen(
                lifecycleOwner = lifecycleOwner,
                modifier = modifier
            )
        }
        permissionDenied -> {
            // Show permission denied screen with retry option
            PermissionDeniedScreen(
                onRetry = {
                    cameraPermissionGranted = true
                    permissionDenied = false
                }
            )
        }
        else -> {
            // Request camera permission
            CameraPermissionScreen(
                onPermissionGranted = {
                    cameraPermissionGranted = true
                    permissionDenied = false
                },
                onPermissionDenied = {
                    permissionDenied = true
                }
            )
        }
    }
}
