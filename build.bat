@echo off
echo Compiling Java files...

:: Compile Java files with correct classpath
javac -cp ".;lib/json-20240303.jar" -d out src/com/weatherapp/*.java

if %errorlevel% neq 0 (
    echo  Compilation failed!
    pause
    exit /b
)

echo  Compilation successful.
echo Running App...

:: Run Java program with correct classpath
java -cp ".;out;lib/json-20240303.jar" com.weatherapp.Main

pause
