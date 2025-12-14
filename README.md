# ShottyoBondhu (সত্যবন্ধু) - True Friend

**ShottyoBondhu** is an Android application designed to be a digital companion, focusing on combating misinformation (fake news) and providing accessibility support for users. ("Shottyo" implies Truth/Real, "Bondhu" implies Friend).

## 🚀 Features

- **Fake News Detection**: Integrated tools and logic to help users verify information and avoid misinformation.
- **Accessibility Service**: Includes a custom Accessibility Service (`ShottyoBondhuService`) to assist users with screen interaction and content reading.
- **Floating Warning Widget**: A system alert component that provides real-time warnings or information overlays.
- **Interactive Quiz**: Educational modules (`QuizActivity`) to test and improve user knowledge.
- **Multi-language Support**: Fully localized for both **Bengali** and **English** speakers (`LocaleHelper`).
- **User Settings**: Customizable preferences via the Settings dashboard.

## 🛠 Tech Stack

- **Language**: Java
- **Platform**: Android
- **Minimum SDK**: API 26 (Android 8.0 Oreo)
- **Architecture**: MVVM / Fragment-based UI
- **Build System**: Gradle (Kotlin DSL)

## 📱 Prerequisites

To run this project locally, you will need:
- Android Studio (Eel or later recommended)
- JDK 11 or higher
- An Android device or emulator running Android 8.0 (API 26) or higher.

## 📥 Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/sheikhsarafathossain/ShottyoBondhu.git
    ```
2.  **Open in Android Studio:**
    - Launch Android Studio.
    - Select **Open** and navigate to the cloned directory.
3.  **Sync Gradle:**
    - Allow Android Studio to download dependencies and sync the project.
4.  **Build & Run:**
    - Connect your device or start an emulator.
    - Click the **Run** (Play) button in the toolbar.

## 📖 Usage

### Enabling Accessibility Service
For the app's core features to function correctly (especially the on-screen assistance), you must enable the Accessibility Service:
1.  Open the app.
2.  Navigate to the **Dashboard** or follow the prompt to **Accessibility Settings**.
3.  Find **ShottyoBondhu** in the list of downloaded services.
4.  Toggle the switch to **ON**.

### Switching Language
- Go to the **Settings** tab.
- Select your preferred language (Bangla / English) to localize the entire interface.

## 📂 Project Structure

```
app/src/main/java/com/team_c/shottyobondhu/
├── MainActivity.java         # Entry point & Navigation host
├── ShottyoBondhuService.java # Accessibility Service logic
├── QuizActivity.java         # Interactive quiz feature
├── HomeFragment.java         # Main dashboard UI
├── DiscoveryFragment.java    # Content discovery & fact checking
├── SettingsFragment.java     # User configuration
└── LocaleHelper.java         # Language context management
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1.  Fork the project
2.  Create your feature branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

Copyright (c) 2025 Sheikh Sarafat Hossain
