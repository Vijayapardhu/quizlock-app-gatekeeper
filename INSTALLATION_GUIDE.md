# Installation Guide - Smart App Gatekeeper

## Prerequisites

### For Android Development
- **Android Studio**: Arctic Fox (2020.3.1) or later
- **Android SDK**: API 26 (Android 8.0) or higher
- **Java**: JDK 8 or higher
- **Device**: Android device with USB debugging enabled OR Android emulator

### For Backend Development
- **Java**: JDK 17 or higher
- **Maven**: 3.6 or higher
- **PostgreSQL**: 12 or higher
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code

## Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/smart-app-gatekeeper.git
cd smart-app-gatekeeper
```

### 2. Android Setup

#### Option A: Using Android Studio (Recommended)
1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to the `app` folder and select it
4. Wait for Gradle sync to complete
5. Connect an Android device or start an emulator
6. Click the "Run" button (green play icon)

#### Option B: Using Command Line
```bash
# Build the project
.\gradlew.bat build

# Install on connected device
.\gradlew.bat installDebug
```

### 3. Backend Setup

#### Option A: Using IDE
1. Open your preferred IDE
2. Import the `backend` folder as a Maven project
3. Configure PostgreSQL database connection in `application.properties`
4. Run the `SmartAppGatekeeperApplication` class

#### Option B: Using Command Line
```bash
cd backend

# Install dependencies and build
mvn clean install

# Run the application
mvn spring-boot:run
```

### 4. Database Setup

#### PostgreSQL Setup
1. Install PostgreSQL 12 or higher
2. Create a new database:
   ```sql
   CREATE DATABASE smartappgatekeeper;
   ```
3. Update `backend/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/smartappgatekeeper
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

## Detailed Setup Instructions

### Android Development Environment

#### 1. Install Android Studio
1. Download from [developer.android.com](https://developer.android.com/studio)
2. Run the installer and follow the setup wizard
3. Install the recommended SDK components

#### 2. Configure Android SDK
1. Open Android Studio
2. Go to File → Settings → Appearance & Behavior → System Settings → Android SDK
3. Install Android 8.0 (API 26) or higher
4. Install Android SDK Build-Tools
5. Install Android SDK Platform-Tools

#### 3. Set up Device/Emulator
**For Physical Device:**
1. Enable Developer Options on your Android device
2. Enable USB Debugging
3. Connect device via USB
4. Allow USB debugging when prompted

**For Emulator:**
1. Open AVD Manager in Android Studio
2. Create a new Virtual Device
3. Choose a device definition (e.g., Pixel 4)
4. Select Android 8.0 or higher
5. Finish setup and start the emulator

### Backend Development Environment

#### 1. Install Java 17
1. Download from [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK](https://adoptium.net/)
2. Set JAVA_HOME environment variable
3. Add Java to PATH

#### 2. Install Maven
1. Download from [maven.apache.org](https://maven.apache.org/download.cgi)
2. Extract to a directory (e.g., C:\apache-maven-3.9.0)
3. Add Maven bin directory to PATH
4. Verify installation: `mvn -version`

#### 3. Install PostgreSQL
1. Download from [postgresql.org](https://www.postgresql.org/download/)
2. Run the installer
3. Set a password for the postgres user
4. Remember the port (default: 5432)

## Troubleshooting

### Common Android Issues

#### Gradle Sync Failed
- **Solution**: Check internet connection and try again
- **Alternative**: Use offline mode in Gradle settings

#### Build Failed - SDK Not Found
- **Solution**: Set ANDROID_HOME environment variable
- **Path**: Usually `C:\Users\YourName\AppData\Local\Android\Sdk`

#### Device Not Recognized
- **Solution**: Install device drivers
- **Alternative**: Use emulator instead

### Common Backend Issues

#### Maven Build Failed
- **Solution**: Check Java version (must be 17+)
- **Command**: `java -version`

#### Database Connection Failed
- **Solution**: Verify PostgreSQL is running
- **Command**: `pg_ctl status`

#### Port Already in Use
- **Solution**: Change port in application.properties
- **Alternative**: Kill process using port 8080

## Verification

### Android App
1. Launch the app on device/emulator
2. Check if main screen loads
3. Verify bottom navigation works
4. Test fragment switching

### Backend API
1. Open browser to `http://localhost:8080/api`
2. Check if API responds
3. Visit `http://localhost:8080/api/swagger-ui.html` for API docs

## Next Steps

After successful installation:

1. **Complete Onboarding**: Follow the app's setup wizard
2. **Grant Permissions**: Allow accessibility and usage stats
3. **Select Target Apps**: Choose which apps to gate
4. **Configure Settings**: Set up quiz preferences
5. **Start Learning**: Begin using the app!

## Support

If you encounter issues:

1. Check the [Troubleshooting](#troubleshooting) section
2. Review the [README.md](README.md) for more details
3. Open an issue on GitHub
4. Contact support: support@smartappgatekeeper.com

---

**Happy Learning! 🚀📚**