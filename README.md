## gemAi - Vision AI Assistant
.

gemAi is a simple yet powerful Android application that leverages the Google Gemini AI model to provide insights and answers based on images. Users can capture a new photo using their camera or select an existing one from their gallery, then ask any question about the image to get an AI-generated response.

## Features

- 📸 **Capture Photos**: Take a photo directly within the app using the camera.
- 🖼️ **Gallery Upload**: Select images from your device's storage.
- 🤖 **Gemini AI Integration**: Uses the `gemini-flash-latest` model for fast and accurate image analysis.
- 💬 **Interactive Chat**: Ask specific questions about the visual content and receive real-time answers.
- 🎨 **Modern UI**: Built with Jetpack Compose and Material 3 for a sleek, responsive experience.

## Prerequisites

Before running the project, ensure you have:

- Android Studio Koala or newer.
- An Android device or emulator running API level 24 (Android 7.0) or higher.
- A Gemini API Key from the [Google AI Studio](https://aistudio.google.com/).

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/gemAi.git
   ```

2. **Setup API Key:**
   The project uses the Firebase AI SDK. Ensure you have properly configured your API key in your project. (Note: For development, you might be using `BuildConfig` or a local properties file to inject the key).

3. **Build and Run:**
   Open the project in Android Studio, sync Gradle, and click **Run**.

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **AI Engine:** Google Gemini (via Firebase AI SDK)
- **Image Loading:** BitmapFactory & Compose UI Graphics
- **Asynchronous Work:** Kotlin Coroutines & Flow

## How it Works

1. **Input**: The user provides an image and a text prompt.
2. **Processing**: The `BakingViewModel` sends the `Bitmap` and the string prompt to the Gemini model using the `generativeModel.generateContent` method.
3. **Response**: The model analyzes the pixels and the text context to generate a descriptive or informative response, which is then displayed in the UI.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
