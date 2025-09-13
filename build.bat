@echo off
echo Building Smart App Gatekeeper...

echo.
echo Checking Java installation...
java -version
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    pause
    exit /b 1
)

echo.
echo Building Android project...
call gradlew.bat build
if %ERRORLEVEL% neq 0 (
    echo ERROR: Android build failed
    pause
    exit /b 1
)

echo.
echo Building Backend project...
cd backend
call mvn clean package
if %ERRORLEVEL% neq 0 (
    echo ERROR: Backend build failed
    pause
    exit /b 1
)

echo.
echo Build completed successfully!
pause
