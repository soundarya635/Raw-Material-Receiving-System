@echo off
echo =========================================================================
echo Starting Spring Boot Backend (com.receivingyard) on Port 8080...
echo =========================================================================
cd /d "%~dp0backend"
call .\mvnw.cmd spring-boot:run

