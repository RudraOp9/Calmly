# Calmly

A modern Android calm music player app that uses Jetpack Compose with Kotlin.
[Download APK](/app/release/app-release.apk)


# Challenges

- Nothing :)

# 🛠️ Tech Stack & Open-source libraries
- Minimum SDK level 27 (android 8.1) to SDK level 36 (android 16)
- [Kotlin](https://kotlinlang.org/) based.
- Jetpack
  - [Compose](https://developer.android.com/jetpack/compose) - A modern toolkit for building native Android UI.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
- [Koin](https://insert-koin.io/) - The pragmatic Kotlin dependency injection framework.

#  Architecture
- Followed the MVVM architecture strictly
```
app/": {
  "MainActivity.kt": "Entry point launching the Compose UI.",
  "CalmlyApp.kt": "Hosts app-wide Composable and navigation.",
  "di/": "Dependency injection setup using Koin.",
  "data/": "Repositories and data source management.",
  "domain/": "Use cases and business logic.",
  "presentation/": {
  "screens/": ["MeditationScreen.kt", "SleepScreen.kt"],
  "components/": ["SoundCard.kt", "NowPlayingBar.kt"],
  "theme/": ["Color.kt", "Type.kt", "Shape.kt"]
  },
  "service/": ["ForegroundMediaService.kt", "NotificationManager.kt"],
  "navigation/": "Navigation graphs and routes.",
  "utils/": "Helper functions and extensions."
  },
  "res/": {
  "raw/": "Audio files in .mp3 format.",
  "drawable/": "Sound thumbnails and UI images.",
  "values/": ["colors.xml", "strings.xml", "themes.xml"]

```



##  Application Flow

- On launch, the Calmly app displays a minimal home screen with Meditation and Sleep tabs.
- Users select a tab to view sound categories in that context (e.g., Forest, Rain, White Noise).
- Tapping a sound card starts seamless, looped playback—the app ensures only one sound plays at a time.
- Users control playback via play/pause on cards or a persistent notification with media controls.
- A timer can be set to auto-stop playback.
- The app remembers the last played sound, restoring it on restart for a consistent, user-friendly experience.

## Demo Video
if video is not visible, please see screenshots directory.
https://github.com/RudraOp9/Calmly/raw/refs/heads/master/screenshots/vid.mkv
<video src="/screenshots/vid.mkv" width="500" height="250" controls type="video/mp4"></video>

##  Screenshots

|                                                            |                                                            |
| :--------------------------------------------------------: | :--------------------------------------------------------: |
| <img src="/screenshots/ss1.png" width="250" height="500" > | <img src="/screenshots/ss2.png" width="250" height="500" > |

|                                                            |
| :--------------------------------------------------------: |
| <img src="/screenshots/ss3.png" width="250" height="500" > |


// download the apk :)
