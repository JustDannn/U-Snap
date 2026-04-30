package com.example.u_snap.ui.camera

import android.content.Context
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * CameraPreviewScreen - Displays a full-screen real-time camera preview using CameraX.
 */
@Composable
fun CameraPreviewScreen(
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    // 1. Ambil context dengan aman
    val context = LocalContext.current

    // 2. Bikin View-nya dulu dan simpen di memori UI (remember)
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    // 3. Taruh LaunchedEffect SEJAJAR di luar AndroidView
    LaunchedEffect(previewView) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).await()

        // Configure preview use case
        val preview = Preview.Builder()
            .setTargetRotation(Surface.ROTATION_0)
            .build()
            .also {
                // Gunakan setSurfaceProvider (lebih aman dari .surfaceProvider = )
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        // Select front-facing camera
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        try {
            // Unbind any previous use cases before binding new ones
            cameraProvider.unbindAll()

            // Bind the preview use case to the camera
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview
            )
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    // 4. AndroidView murni cuma buat nampilin View yang udah dibikin di atas
    AndroidView(
        factory = { previewView },
        modifier = modifier.fillMaxSize()
    )
}

/**
 * Extension function to convert ListenableFuture to suspend function for coroutines.
 */
private suspend inline fun <reified T> com.google.common.util.concurrent.ListenableFuture<T>.await(): T {
    return suspendCancellableCoroutine { continuation ->
        addListener({
            try {
                continuation.resume(get())
            } catch (e: Exception) {
                continuation.resumeWith(Result.failure(e))
            }
        }, com.google.common.util.concurrent.MoreExecutors.directExecutor())
    }
}