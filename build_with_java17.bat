@echo off
echo ========================================
echo Smart App Gatekeeper - APK Builder
echo Using Java 17 for compatibility
echo ========================================

echo.
echo Setting up Java 17 environment...

REM Try to find Java 17 installations
set JAVA17_FOUND=0

REM Check common Java 17 installation paths
if exist "C:\Program Files\Eclipse Adoptium\jdk-17*" (
    for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do (
        set JAVA_HOME=%%i
        set JAVA17_FOUND=1
        echo Found Java 17 at: %%i
        goto :found_java17
    )
)

if exist "C:\Program Files\Java\jdk-17*" (
    for /d %%i in ("C:\Program Files\Java\jdk-17*") do (
        set JAVA_HOME=%%i
        set JAVA17_FOUND=1
        echo Found Java 17 at: %%i
        goto :found_java17
    )
)

if exist "C:\Program Files\OpenJDK\jdk-17*" (
    for /d %%i in ("C:\Program Files\OpenJDK\jdk-17*") do (
        set JAVA_HOME=%%i
        set JAVA17_FOUND=1
        echo Found Java 17 at: %%i
        goto :found_java17
    )
)

:found_java17
if %JAVA17_FOUND%==0 (
    echo.
    echo ERROR: Java 17 not found!
    echo.
    echo Please install Java 17 from one of these locations:
    echo 1. Eclipse Adoptium: https://adoptium.net/temurin/releases/?version=17
    echo 2. Oracle JDK: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
    echo 3. OpenJDK: https://jdk.java.net/17/
    echo.
    echo After installation, run this script again.
    echo.
    pause
    exit /b 1
)

echo Setting JAVA_HOME to: %JAVA_HOME%
set PATH=%JAVA_HOME%\bin;%PATH%

echo.
echo Verifying Java version...
java -version
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java 17 not working properly
    pause
    exit /b 1
)

echo.
echo Java 17 is ready! Building APK...
echo.

REM Clean previous builds
echo Cleaning previous builds...
call gradlew.bat clean
if %ERRORLEVEL% neq 0 (
    echo WARNING: Clean failed, continuing anyway...
)

echo.
echo Building APK with Java 17...
call gradlew.bat assembleDebug
if %ERRORLEVEL% neq 0 (
    echo.
    echo BUILD FAILED!
    echo.
    echo Common solutions:
    echo 1. Make sure Android SDK is installed
    echo 2. Check if licenses are accepted
    echo 3. Try using Android Studio instead
    echo.
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
