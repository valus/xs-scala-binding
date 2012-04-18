@echo off

@REM Internal options, always specified
set INTERNAL_OPTS=-Dfile.encoding=UTF-8 -Xmx512M -XX:MaxPermSize=256m

@REM Add 64bit specific option
java -version 2>&1 | find "64-Bit" >nul:
if not errorlevel 1 (
  set INTERNAL_OPTS=%INTERNAL_OPTS% -XX:+UseCompressedOops -XX:ReservedCodeCacheSize=128m
)

@REM Default options, if nothing is specified
set DEFAULT_OPTS=

java %INTERNAL_OPTS% -jar "%~dp0\project\sbt-launch.jar" %*