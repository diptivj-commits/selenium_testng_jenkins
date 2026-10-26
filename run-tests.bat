@echo off
cd /d "%~dp0"
call mvn -B -ntp clean test -Dheadless=true
exit /b %ERRORLEVEL%
