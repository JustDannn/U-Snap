# Instructions for Claude (Expert Android Developer)

Act as a Senior Android Developer specializing in Kotlin, Jetpack Compose, and Computer Vision integration. 

I am building an app called U-Snap. Please read the attached `srs.md` for the project context, technical specifications, and architectural constraints.

Your task is to generate the foundational Kotlin code to achieve the MVP Core Loop described in the SRS. We will do this step-by-step. 

**DO NOT generate the entire application at once.** Please start by executing **Step 1** only. Wait for my confirmation and feedback before proceeding to the next steps.

## Execution Plan:

**Step 1: Project Setup & Dependencies**
Provide the necessary Gradle dependencies (CameraX, Jetpack Compose, MediaPipe Tasks Vision, Coroutines) and AndroidManifest.xml configurations (Permissions) required for this MVP.

**Step 2: The CameraX & Compose Layer**
Generate a Jetpack Compose screen that handles camera permissions and displays a full-screen `PreviewUseCase` using CameraX (front camera). Ensure the code is modular and uses modern Compose best practices.

**Step 3: The MediaPipe Analyzer**
Create a custom `ImageAnalysis.Analyzer` class that takes the frames from CameraX, converts them to the format required by MediaPipe, and runs the Face Landmarker model. It should expose the resulting landmark coordinates via a Kotlin Flow or State.

**Step 4: The Canvas Overlay**
Update the Compose screen to overlay a `Canvas` on top of the Camera Preview. Draw small red circles on the coordinates extracted from Step 3 to visually verify the detection.

**Begin with Step 1.** Provide the code and a brief explanation of the setup.
