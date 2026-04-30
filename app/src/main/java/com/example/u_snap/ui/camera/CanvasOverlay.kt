package com.example.u_snap.ui.camera

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * CanvasOverlay - Renders landmark points as red circles on top of camera preview
 * 
 * @param landmarks List of facial landmark points with normalized coordinates (0.0 - 1.0)
 * @param modifier Modifier for layout customization
 */
@Composable
fun CanvasOverlay(
    data: FaceLandmarkData,  // <-- ganti parameter
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawLandmarks(data)
    }
}

private fun DrawScope.drawLandmarks(data: FaceLandmarkData) {
    if (data.landmarks.isEmpty()) return

    val canvasW = size.width
    val canvasH = size.height
    val imgW = data.imageWidth.toFloat()
    val imgH = data.imageHeight.toFloat()

    // Hitung scale & offset persis kayak FILL_CENTER
    val scaleX = canvasW / imgW
    val scaleY = canvasH / imgH
    val scale = maxOf(scaleX, scaleY)  // FILL = ambil yang lebih besar (crop)

    val scaledW = imgW * scale
    val scaledH = imgH * scale

    // Offset karena gambar di-crop tapi di-center
    val offsetX = (canvasW - scaledW) / 2f
    val offsetY = (canvasH - scaledH) / 2f

    for (landmark in data.landmarks) {
        val pixelX = landmark.x * scaledW + offsetX
        val pixelY = landmark.y * scaledH + offsetY

        drawCircle(
            color = Color.Red,
            radius = 6f,
            center = Offset(pixelX, pixelY)
        )
    }
}