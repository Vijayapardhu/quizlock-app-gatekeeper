@echo off
echo ========================================
echo Smart App Gatekeeper - Release Builder
echo ========================================

echo.
echo Building release APK...

REM Try to build with current Java version
call gradlew.bat assembleRelease --no-daemon --max-workers=1
if %ERRORLEVEL% neq 0 (
    echo.
    echo Build failed with current Java version.
    echo.
    echo Solutions:
    echo 1. Install Java 17 from https://adoptium.net/temurin/releases/?version=17
    echo 2. Use Android Studio to build the project
    echo 3. Use the build_with_java17.bat script after installing Java 17
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo BUILD SUCCESSFUL!
echo ========================================
echo.
echo APK Location: app\build\outputs\apk\release\app-release.apk
echo.

if exist "app\build\outputs\apk\release\app-release.apk" (
    echo APK file found! Size:
    dir "app\build\outputs\apk\release\app-release.apk" | findstr "app-release.apk"
    echo.
    echo You can now install this APK on your Android device!
) else (
    echo WARNING: APK file not found in expected location
)

echo.
pause
