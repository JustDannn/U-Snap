package com.example.u_snap.ui.camera

import android.content.Context
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * CameraWithAnalyzerScreen - Enhanced camera screen with MediaPipe analyzer and Canvas overlay
 * 
 * This composable:
 * 1. Initializes CameraX with Preview use case (front camera)
 * 2. Adds ImageAnalysis use case for real-time frame processing with FaceLandmarkAnalyzer
 * 3. Overlays a Canvas on top of the preview to draw detected facial landmarks
 * 
 * @param lifecycleOwner The lifecycle owner (typically the Activity)
 * @param modifier Modifier for layout customization
 */
@Composable
fun CameraWithAnalyzerScreen(
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // State to hold detected landmarks
    val landmarkState = remember { mutableStateOf(FaceLandmarkData(emptyList())) }
    
    // Analyzer instance
    val analyzer = remember {
        FaceLandmarkAnalyzer(context, landmarkState)
    }
    
    // PreviewView instance
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    // Initialize camera on composition
    LaunchedEffect(previewView) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).await()

        // Configure preview use case
        val preview = Preview.Builder()
            .setTargetRotation(Surface.ROTATION_0)
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

        // Configure image analysis use case for landmark detection
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetRotation(Surface.ROTATION_0)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(
                    // Executor: Process frames on a background thread pool
                    { command ->
                        command.run()  // Or use a dedicated Executor for heavier processing
                    },
                    analyzer
                )
            }

        // Select front-facing camera
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        try {
            cameraProvider.unbindAll()

            // Bind both Preview and ImageAnalysis use cases
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (exc: Exception) {
            exc.printStackTrace()
        }
    }

    // Box to layer Camera preview + Canvas overlay
    Box(modifier = modifier.fillMaxSize()) {
        // Camera preview view
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Canvas overlay with red landmark dots
        CanvasOverlay(
            data = landmarkState.value,
            modifier = Modifier.fillMaxSize()
        )
    }
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
