@echo off
REM Personal Finance Manager - Windows compile and run script
REM Requires JDK 17+ on PATH

if not exist out mkdir out

echo Compiling...
javac -d out src\com\finance\*.java
if %errorlevel% neq 0 (
    echo Compilation failed. Please install JDK 17+ and check PATH.
    pause
    exit /b 1
)

echo Running...
java -cp out com.finance.Main
