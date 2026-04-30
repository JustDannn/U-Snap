# Software Requirements Specification (MVP) - U-Snap

## 1. Project Overview
U-Snap is an Android-based interactive camera application. The ultimate goal of the project is to bridge native Android facial/gesture detection with a Unity 3D rendering engine. 

For this Minimum Viable Product (MVP) phase, the focus is strictly on establishing a robust native Android application that can capture real-time camera feed and accurately extract facial landmarks using Google's MediaPipe.

## 2. Tech Stack & Specifications
*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Modern Declarative UI)
*   **Camera API:** CameraX (Preview and ImageAnalysis use cases)
*   **Computer Vision:** MediaPipe Tasks Vision (`com.google.mediapipe:tasks-vision`) - specifically Face Landmarker.
*   **Minimum API Level:** API 24 (Android 7.0)

## 3. Functional Requirements (MVP Core Loop)
1.  **Camera Permission & Initialization:** The app must prompt the user for camera permissions upon launch. Once granted, it must initialize the front-facing camera.
2.  **Full-Screen Preview:** The app must display a real-time, full-screen camera preview using Jetpack Compose without noticeable latency.
3.  **Real-Time Landmark Detection:** The app must analyze incoming camera frames in real-time, process them through the MediaPipe Face Landmarker model, and extract the 3D coordinates (X, Y, Z) of the facial landmarks.
4.  **Visual Debugging (Canvas):** Instead of rendering 3D objects at this stage, the app must draw a 2D Canvas overlay on top of the camera preview. It should draw simple visible markers (e.g., red dots) on the detected facial landmarks to visually verify the accuracy of the MediaPipe extraction.

## 4. Architectural Constraints
*   Ensure the UI thread is not blocked by image processing. Use background threads/coroutines for CameraX `ImageAnalysis` and MediaPipe processing.
*   Keep the logic modular. Separate the UI layer (Compose), Camera initialization logic, and the MediaPipe processing logic.
