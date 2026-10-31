@echo off
echo =========================================================================
echo Starting PostgreSQL 18 Cluster on Port 5433...
echo =========================================================================
"C:\Program Files\PostgreSQL\18\bin\pg_ctl.exe" -D "%~dp0database\pgdata" -o "-p 5433" -l "%~dp0database\postgres.log" start
echo PostgreSQL started on port 5433.

