# QuizLock: Smart App Gatekeeper

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

QuizLock is an innovative Android application that combines productivity with learning by locking distracting apps and requiring users to answer quiz questions to unlock them. Transform your screen time into learning time!

## 🚀 Features

### Core Functionality
- **Smart App Interception**: Uses accessibility service to detect when restricted apps are opened
- **Quiz-Based Unlocking**: Answer educational questions to gain access to locked apps
- **Session Management**: Time-limited access with countdown timers and notifications
- **Emergency Unlock**: Bypass option for urgent situations (logged for accountability)

### Educational Features
- **Dynamic Quiz Generation**: Integration with Gemini API for fresh, topic-based questions
- **Multiple Topics**: Math, Programming, General Knowledge, Vocabulary, Fitness, Science, History, Geography
- **Adaptive Difficulty**: Questions get easier after wrong answers to maintain engagement
- **Offline Mode**: Fallback quiz questions when internet is unavailable

### Gamification & Analytics
- **Streak Tracking**: Daily learning streaks with motivational notifications
- **Usage Statistics**: Detailed dashboard with weekly usage patterns
- **Topic Performance**: Track your progress across different subjects
- **Rewards System**: Achievements for maintaining streaks and answering questions

### User Experience
- **Apple-Inspired Design**: Clean, minimal interface with Material Design 3
- **Dark Mode Support**: Elegant themes for day and night usage
- **Smooth Animations**: Lottie animations and transitions for visual appeal
- **Accessibility**: Full accessibility service integration and support

## 🏗️ Architecture

### MVVM Pattern
- **Model**: Room database with SQLite for local data persistence
- **View**: Activities and Fragments with ViewBinding
- **ViewModel**: Business logic and LiveData observers

### Technology Stack
- **Language**: Java (100% Java implementation)
- **Database**: Room ORM with SQLite
- **Networking**: Retrofit with OkHttp for Gemini API
- **Background Tasks**: WorkManager for timers and notifications
- **UI Framework**: Material Design 3 components
- **Animations**: Lottie for smooth animations

### Project Structure
```
app/src/main/java/com/vijayapardhu/quizlock/
├── db/                 # Room database entities, DAOs, and database
├── network/           # Retrofit API interfaces and models
├── service/           # Background services (Accessibility, Timer)
├── ui/               # Activities, Fragments, and Adapters
│   ├── adapters/     # RecyclerView adapters
│   ├── fragments/    # Main app fragments
│   └── fragments/onboarding/  # Onboarding flow
├── utils/            # Utility classes and constants
└── viewmodel/        # ViewModels for MVVM architecture
```

## 📱 Screenshots

*Coming soon - app screenshots will be added here*

## 🛠️ Installation & Setup

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API level 24 or higher
- Gemini API key (optional, for dynamic quiz generation)

### Building the Project
1. Clone the repository:
```bash
git clone https://github.com/Vijayapardhu/quizlock-app-gatekeeper.git
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Add your Gemini API key (optional):
   - Go to Settings in the app
   - Enter your Gemini API key for dynamic quiz generation
   - Without API key, the app uses offline questions

5. Build and run the application

### Required Permissions
The app requires the following permissions:
- **Accessibility Service**: To detect when restricted apps are opened
- **Internet**: For Gemini API quiz generation
- **Foreground Service**: For session timers
- **System Alert Window**: For quiz overlay screens
- **Notifications**: For session alerts and reminders

## 🎯 Usage

### Initial Setup
1. Complete the onboarding flow:
   - Welcome screen introduction
   - Select apps to restrict
   - Choose quiz topics
   - Set daily usage limits

2. Enable Accessibility Service:
   - Go to Settings → Accessibility
   - Enable QuizLock App Gatekeeper service

### Daily Usage
1. When you try to open a restricted app, QuizLock intercepts it
2. Answer the quiz question correctly to unlock the app
3. Enjoy a timed session (default 30 minutes)
4. When the session ends, the app locks again

### Dashboard Features
- View daily usage statistics
- Track your learning streaks
- Monitor weekly progress
- See topic-wise performance

## 🔧 Configuration

### Customizing Quiz Topics
- Math: Arithmetic, algebra, geometry problems
- Programming: Basic coding concepts and syntax
- General Knowledge: World facts, current events
- Vocabulary: Word meanings and synonyms
- Fitness: Health and exercise questions
- Science: Physics, chemistry, biology basics
- History: Historical events and figures
- Geography: Countries, capitals, landmarks

### Session Settings
- Default session duration: 30 minutes
- Cooldown period: 5 minutes after wrong answers
- Daily limits: Customizable per app
- Emergency unlock: Available with logging

## 🤝 Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java coding conventions
- Use MVVM architecture pattern
- Write meaningful commit messages
- Update documentation for new features
- Test on multiple Android versions

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Material Design 3 for the beautiful UI components
- Lottie for smooth animations
- Room Database for robust data persistence
- Retrofit for reliable networking
- Gemini AI for dynamic quiz generation

## 📞 Support

If you encounter any issues or have questions:
- Create an issue on GitHub
- Check the [Wiki](../../wiki) for troubleshooting
- Review existing [Issues](../../issues) and [Discussions](../../discussions)

## 🔮 Future Enhancements

- [ ] Social features - compete with friends
- [ ] More quiz types (voice, image-based)
- [ ] Advanced analytics and insights
- [ ] Custom quiz creation
- [ ] Parental controls
- [ ] Cross-platform sync
- [ ] AI-powered difficulty adjustment
- [ ] Integration with educational platforms

---

**QuizLock** - Transform your screen time into learning time! 📚📱✨
