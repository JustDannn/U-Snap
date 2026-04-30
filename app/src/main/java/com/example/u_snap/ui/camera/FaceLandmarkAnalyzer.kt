package com.example.u_snap.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult

data class LandmarkPoint(
    val x: Float,
    val y: Float,
    val z: Float
)
data class FaceLandmarkData(
    val landmarks: List<LandmarkPoint>,
    val imageWidth: Int = 1,
    val imageHeight: Int = 1
)
class FaceLandmarkAnalyzer(
    private val context: Context,
    private val landmarkState: MutableState<FaceLandmarkData> = mutableStateOf(FaceLandmarkData(emptyList()))
) : ImageAnalysis.Analyzer {
    private var lastImageWidth = 1
    
    private var lastImageHeight = 1
    private var faceLandmarker: FaceLandmarker? = null

    init {
        // Run init on a background thread
        Thread {
            try {
                val baseOptions = BaseOptions.builder()
                    .setModelAssetPath("models/face_landmarker.task")
                    .build()

                val options = FaceLandmarker.FaceLandmarkerOptions.builder()
                    .setBaseOptions(baseOptions)
                    .setRunningMode(RunningMode.LIVE_STREAM)
                    .setResultListener(this::onFaceLandmarkResult)
                    .setErrorListener(this::onError)
                    .build()

                faceLandmarker = FaceLandmarker.createFromOptions(context, options)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun analyze(imageProxy: ImageProxy) {
        val landmarker = faceLandmarker ?: run {
            imageProxy.close()
            return
        }

        try {
            // toBitmap() dari CameraX, bye-bye kode YUV manual
            val bitmap = imageProxy.toBitmap()

            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            }
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            lastImageWidth = rotatedBitmap.width
            lastImageHeight = rotatedBitmap.height
            val mpImage = BitmapImageBuilder(rotatedBitmap).build()

            val timestampMs = imageProxy.imageInfo.timestamp / 1_000_000

            landmarker.detectAsync(mpImage, timestampMs)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            imageProxy.close()
        }
    }

    private fun onFaceLandmarkResult(result: FaceLandmarkerResult, input: MPImage) {
        if (result.faceLandmarks().isNotEmpty()) {
            val landmarks = result.faceLandmarks()[0].map { landmark ->
                LandmarkPoint(
                    x = landmark.x(),
                    y = landmark.y(),
                    z = landmark.z()
                )
            }
            landmarkState.value = FaceLandmarkData(
    landmarks = landmarks,
    imageWidth = lastImageWidth,
    imageHeight = lastImageHeight
)

        } else {
            landmarkState.value = FaceLandmarkData(emptyList())
        }
    }

    private fun onError(error: RuntimeException) {
        error.printStackTrace()
    }

    fun release() {
        faceLandmarker?.close()
        faceLandmarker = null
    }
}