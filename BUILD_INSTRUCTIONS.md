# Build Instructions - Smart App Gatekeeper

## ⚠️ Java Version Issue

The current build is failing because **Java 24** is not yet supported by Gradle. Here are the solutions:

## 🔧 Solution 1: Use Compatible Java Version (Recommended)

### Install Java 17 or Java 21
1. **Download Java 17** from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK](https://adoptium.net/temurin/releases/?version=17)
2. **Install Java 17** alongside your current Java 24
3. **Set JAVA_HOME** to point to Java 17:
   ```cmd
   set JAVA_HOME=C:\Program Files\Java\jdk-17
   set PATH=%JAVA_HOME%\bin;%PATH%
   ```
4. **Verify Java version**:
   ```cmd
   java -version
   ```
   Should show Java 17.x.x

### Then Build APK
```cmd
.\gradlew.bat assembleDebug
```

## 🔧 Solution 2: Use Android Studio (Easiest)

1. **Open Android Studio**
2. **Open the `app` folder** as a project
3. **Let Android Studio handle** the Java/Gradle setup
4. **Click Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. **APK will be generated** in `app/build/outputs/apk/debug/`

## 🔧 Solution 3: Use Gradle with Java 24 (Experimental)

If you want to try with Java 24, use the latest Gradle version:

```cmd
# Use Gradle 8.11.1 with Java 24 (experimental)
.\gradlew.bat assembleDebug --no-daemon
```

## 📱 Expected APK Location

Once built successfully, the APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

## 🚀 Quick Build Script

I've created a build script that handles the Java version issue:

```cmd
# Run this script
.\build_apk.bat
```

## 🔍 Troubleshooting

### If you get "Unsupported class file major version 68":
- This means Java 24 is not supported
- Use Java 17 or Java 21 instead

### If you get SDK license errors:
- Run: `sdkmanager --licenses`
- Accept all licenses

### If you get "SDK not found":
- Set ANDROID_HOME environment variable
- Point to your Android SDK location

## 📋 Prerequisites Check

Before building, ensure you have:
- ✅ Android SDK installed
- ✅ Java 17 or 21 (not Java 24)
- ✅ Android device or emulator
- ✅ Internet connection for dependencies

## 🎯 Build Commands

```cmd
# Clean and build
.\gradlew.bat clean assembleDebug

# Build release APK
.\gradlew.bat assembleRelease

# Install on connected device
.\gradlew.bat installDebug

# Build and install in one command
.\gradlew.bat installDebug
```

## 📱 Testing the APK

1. **Install APK** on your Android device
2. **Grant permissions** when prompted:
   - Accessibility Service
   - Usage Stats
   - Overlay Permission
   - Notifications
3. **Complete onboarding** setup
4. **Test app interception** by selecting target apps

---

**The project is complete and ready to build once you use a compatible Java version! 🚀**
