# Grama-urja
GramaUrja is an Android app built with Kotlin that helps rural farmers monitor electricity availability. It features real-time power status tracking, a pump timer for irrigation scheduling, power availability predictions, weather updates, zone-wise search, and a history log with a clean bottom-navigation interface

# GramaUrja ⚡

GramaUrja is an Android app built with Kotlin that helps rural farmers monitor electricity availability. It features real-time power status tracking, a pump timer for irrigation scheduling, power availability predictions, weather updates, zone-wise search, and a history log with a clean bottom-navigation interface.

---

## Features

- **Home Dashboard** — Live power status (ON/OFF) with animated indicators, weather info, and AI-based power availability predictions
- **Pump Timer** — Schedule and track irrigation pump sessions based on available power windows
- **History Log** — View past power on/off events with timestamps
- **Zone Search** — Search and browse electricity zones across regions
- **Splash & Zone Selection** — Smooth onboarding with zone selection on first launch
- **Firebase Integration** — Real-time database sync and push notifications via Firebase

---

## Screenshots

> _Add screenshots here_

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 35 (Android 15) |
| Architecture | Fragment-based with BottomNavigationView |
| Backend | Firebase Realtime Database |
| Notifications | Firebase Cloud Messaging (FCM) |
| UI | Material Design 3 |

---

## Project Structure

```
app/src/main/java/com/gramaurja/gramaurja/
├── MainActivity.kt           # Entry point, bottom nav controller
├── SplashActivity.kt         # Splash screen
├── ZoneSelectionActivity.kt  # Zone picker on first launch
├── HomeFragment.kt           # Dashboard with power status & predictions
├── PumpTimerFragment.kt      # Pump scheduling & timer
├── HistoryFragment.kt        # Power event history
└── SearchFragment.kt         # Zone search
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11+
- A Firebase project with Realtime Database and FCM enabled

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/YOUR_USERNAME/GramaUrja.git
   cd GramaUrja
   ```

2. Add your `google-services.json` file to the `app/` directory (get it from your Firebase Console)

3. Open the project in Android Studio and let Gradle sync

4. Run on a device or emulator
   ```bash
   ./gradlew installDebug
   ```

---

## Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use an existing one
3. Add an Android app with package name `com.gramaurja.gramaurja`
4. Download `google-services.json` and place it in the `app/` folder
5. Enable **Realtime Database** and **Cloud Messaging** in the Firebase Console

---

## Build

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
