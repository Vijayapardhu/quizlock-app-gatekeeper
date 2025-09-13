@echo off
echo ========================================
echo Smart App Gatekeeper - APK Builder
echo ========================================

echo.
echo Checking Java version...
java -version
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo Checking if Java 24 is being used...
java -version 2>&1 | findstr "24" >nul
if %ERRORLEVEL% equ 0 (
    echo WARNING: Java 24 detected! This will cause build issues.
    echo.
    echo RECOMMENDED SOLUTIONS:
    echo 1. Use Java 17 or Java 21 (RECOMMENDED)
    echo 2. Use Android Studio (EASIEST)
    echo 3. Use the build_with_java17.bat script
    echo.
echo Continuing with Java 24...
)

echo.
echo Cleaning previous builds...
call gradlew.bat clean
if %ERRORLEVEL% neq 0 (
    echo ERROR: Clean failed
    pause
    exit /b 1
)

echo.
echo Building APK...
call gradlew.bat assembleDebug --no-daemon
if %ERRORLEVEL% neq 0 (
    echo.
    echo BUILD FAILED!
    echo.
    echo Common solutions:
    echo 1. Use Java 17 or Java 21 instead of Java 24
    echo 2. Install Android SDK and accept licenses
    echo 3. Use Android Studio to build instead
    echo.
    echo See BUILD_INSTRUCTIONS.md for detailed help
    pause
    exit /b 1
)

echo.
echo ========================================
echo BUILD SUCCESSFUL!
echo ========================================
echo.
echo APK Location: app\build\outputs\apk\debug\app-debug.apk
echo.

if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo APK file found! Size:
    dir "app\build\outputs\apk\debug\app-debug.apk" | findstr "app-debug.apk"
    echo.
    echo You can now install this APK on your Android device!
) else (
    echo WARNING: APK file not found in expected location
)

echo.
echo Next steps:
echo 1. Transfer APK to your Android device
echo 2. Enable "Install from unknown sources" in device settings
echo 3. Install the APK
echo 4. Grant required permissions when prompted
echo 5. Complete the onboarding setup
echo.
pause