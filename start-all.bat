@echo off
echo =========================================================================
echo Launching Raw Material Receiving Yard & QC System...
echo =========================================================================

echo 1. Starting PostgreSQL...
start "PostgreSQL Database (Port 5433)" cmd /k "%~dp0start-postgres.bat"
timeout /t 3 /nobreak > nul

echo 2. Starting Spring Boot Backend...
start "Receiving Yard Backend (Port 8080)" cmd /k "%~dp0start-backend.bat"
timeout /t 5 /nobreak > nul

echo 3. Starting React Frontend...
start "Receiving Yard Frontend (Port 5173)" cmd /k "%~dp0start-frontend.bat"

echo.
echo All services launched!
echo - Frontend: http://localhost:5173
echo - Backend Health: http://localhost:8080/api/health
echo - Database: localhost:5433 (receivingyard_db)
echo.

