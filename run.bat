@echo off
title Student Management System
echo Building Student Management System...
call mvn -q clean package
if errorlevel 1 (
    echo.
    echo BUILD FAILED. Check that Java and Maven are installed.
    pause
    exit /b 1
)
echo.
echo Starting Student Management System...
java -jar target\student-management-1.0.0.jar
echo.
pause
