@echo off
setlocal enabledelayedexpansion

REM ==============================
REM CONFIGURATION
REM ==============================

REM Chemin du projet Maven
set "PROJECT_DIR=D:\GitHub\front-office\frontoffice"

REM Nom de l'artifact Maven (doit correspondre à <artifactId>)
set "ARTIFACT_ID=frontoffice"
set "VERSION=1.0.0"

REM Nom du WAR généré par Maven
set "WAR_FILE=%ARTIFACT_ID%-%VERSION%.war"

REM Nom du dossier de déploiement dans Tomcat (sans .war)
REM ⚠️ On force ici le nom du WAR / dossier pour avoir URL /frontoffice/
set "DEPLOY_NAME=%ARTIFACT_ID%"

REM Chemin Tomcat 11
set "TOMCAT_HOME=C:\Program Files\Tomcat\apache-tomcat-11.0.13"
set "TOMCAT_WEBAPPS=%TOMCAT_HOME%\webapps"

REM ==============================
REM BUILD MAVEN
REM ==============================

echo === MAVEN CLEAN PACKAGE ===
cd /d "%PROJECT_DIR%"

echo Exécution de mvn clean package...
call mvn clean package

if not %ERRORLEVEL% == 0 (
    echo.
    echo ❌ Build Maven échoué. Code d'erreur: %ERRORLEVEL%
    pause
    exit /b %ERRORLEVEL%
)

echo ✅ Build Maven terminé avec succès.

REM Vérifier que le WAR a été généré
echo.
echo Vérification du fichier WAR généré...
if exist "target\%WAR_FILE%" (
    echo ✅ Fichier WAR trouvé: target\%WAR_FILE%
) else (
    echo.
    echo ❌ FICHIER WAR INTROUVABLE: target\%WAR_FILE%
    echo Liste des fichiers WAR dans target\:
    dir "target\*.war" 2>nul
    pause
    exit /b 1
)

REM ==============================
REM DEPLOYMENT
REM ==============================

echo.
echo === DEPLOY TO TOMCAT ===

if not exist "%TOMCAT_WEBAPPS%" (
    echo ❌ Dossier Tomcat introuvable: %TOMCAT_WEBAPPS%
    pause
    exit /b 1
)

REM Supprimer ancienne version déployée (dossier + WAR)
if exist "%TOMCAT_WEBAPPS%\%DEPLOY_NAME%" (
    rd /s /q "%TOMCAT_WEBAPPS%\%DEPLOY_NAME%"
)

if exist "%TOMCAT_WEBAPPS%\%DEPLOY_NAME%.war" (
    del /f /q "%TOMCAT_WEBAPPS%\%DEPLOY_NAME%.war"
)

REM Copier le WAR en forçant le nom pour URL /front-office/
copy /y "target\%WAR_FILE%" "%TOMCAT_WEBAPPS%\%DEPLOY_NAME%.war" >nul

if exist "%TOMCAT_WEBAPPS%\%DEPLOY_NAME%.war" (
    echo ✅ WAR copié avec succès en tant que %DEPLOY_NAME%.war
) else (
    echo ❌ Erreur lors de la copie du WAR.
    pause
    exit /b 1
)

echo.
echo ============================================
echo ✅ DÉPLOIEMENT TERMINÉ AVEC SUCCÈS !
echo ============================================
echo Application accessible à :
echo http://localhost:8080/%DEPLOY_NAME%/
echo.
