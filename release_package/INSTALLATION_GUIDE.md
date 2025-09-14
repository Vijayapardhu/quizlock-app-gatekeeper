# 📱 Smart App Gatekeeper - Installation Guide

## Version 2.0.0 - "Learning Revolution"

---

## 🚀 **Quick Installation**

### **For End Users:**
1. **Download** `SmartAppGatekeeper-v2.0.0-signed.apk`
2. **Enable Unknown Sources** in your Android settings
3. **Install** the APK file
4. **Launch** and complete the onboarding process

### **For Developers:**
1. **Clone Repository**: `git clone https://github.com/Vijayapardhu/quizlock-app-gatekeeper.git`
2. **Open in Android Studio**
3. **Sync Project** with Gradle files
4. **Build & Run** on device or emulator

---

## 📋 **System Requirements**

- **Android Version**: 7.0 (API level 24) or higher
- **RAM**: 2GB minimum, 4GB recommended
- **Storage**: 100MB for app + additional space for data
- **Permissions Required**:
  - Storage (for data backup)
  - Network (for cloud sync)
  - Accessibility (for app blocking features)

---

## 🔧 **Installation Steps**

### **Step 1: Enable Unknown Sources**
1. Go to **Settings** > **Security** (or **Privacy**)
2. Enable **"Install from Unknown Sources"** or **"Unknown Apps"**
3. Select your file manager app and enable installation

### **Step 2: Install the APK**
1. Download `SmartAppGatekeeper-v2.0.0-signed.apk`
2. Open your file manager
3. Navigate to the downloaded APK
4. Tap on the APK file
5. Tap **"Install"**
6. Wait for installation to complete

### **Step 3: First Launch**
1. Open **Smart App Gatekeeper** from your app drawer
2. Complete the **onboarding process**
3. Set your **learning preferences**
4. Take a **quick assessment quiz**
5. Choose your **first learning path**

---

## 🛠️ **Developer Setup**

### **Prerequisites**
- Android Studio (latest version)
- JDK 8 or higher
- Android SDK (API 24+)
- Git

### **Build Instructions**
```bash
# Clone the repository
git clone https://github.com/Vijayapardhu/quizlock-app-gatekeeper.git
cd quizlock-app-gatekeeper

# Build debug version
./gradlew assembleDebug

# Build release version
./gradlew assembleRelease
```

### **Signing Configuration**
- **Debug**: Uses default debug keystore
- **Release**: Uses `release.keystore` with alias `smartapp`
- **Keystore Password**: `smartapp2025`
- **Key Password**: `smartapp2025`

---

## 🔒 **Security Information**

### **APK Signing**
- **Signed with**: RSA 2048-bit key
- **Validity**: 10,000 days
- **Certificate**: Self-signed for development
- **Keystore**: Included in release package

### **Permissions Explained**
- **Storage**: Required for saving quiz results and learning progress
- **Network**: Used for cloud backup and data synchronization
- **Accessibility**: Enables app blocking and usage monitoring features

---

## 🐛 **Troubleshooting**

### **Common Issues**

**Installation Failed:**
- Ensure "Unknown Sources" is enabled
- Check if you have enough storage space
- Verify the APK file is not corrupted

**App Crashes on Launch:**
- Clear app data and try again
- Restart your device
- Check if your Android version is supported (7.0+)

**Quiz Questions Not Loading:**
- Check your internet connection
- Clear app cache in Settings > Apps > Smart App Gatekeeper > Storage
- Use the debug tools (long-press Settings title)

**Database Issues:**
- Use the built-in debug tools to reset the database
- Clear app data and restart
- Check the release notes for known issues

---

## 📞 **Support & Help**

### **Built-in Debug Tools**
1. Open the app
2. Go to **Settings**
3. **Long-press** on the Settings title
4. Select **Debug Tools** from the menu
5. Use **Clear Data** or **Reset Database** as needed

### **Data Backup**
- Use **Settings** > **Data Export** to backup your progress
- Enable **Cloud Backup** for automatic data sync
- Export data before major updates

### **Feature Requests**
- Use the in-app feedback system
- Report issues through the debug tools
- Check the release notes for upcoming features

---

## 📊 **App Information**

- **Package Name**: `com.smartappgatekeeper`
- **Version Code**: 2
- **Version Name**: 2.0.0
- **Target SDK**: 34
- **Minimum SDK**: 26
- **File Size**: ~9.8 MB

---

## 🎯 **Getting Started After Installation**

1. **Complete Onboarding**: Set up your learning profile
2. **Take Assessment Quiz**: Get personalized recommendations
3. **Choose Learning Path**: Select a course that interests you
4. **Customize Settings**: Personalize your learning experience
5. **Start Learning**: Begin your educational journey!

---

**Happy Learning! 🎓✨**

*For technical support, use the built-in debug tools or refer to the release notes.*
