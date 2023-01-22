@echo off

rem variables
set folderPath=C:\Users\%username%\desktop
set repositoryName=SimpleJavaFxTestBase

rem init text
echo ====================================================================================
echo Hello and welcome to the great SimpleJavaFxTestBase JAR compiler script!
echo ====================================================================================

echo:
echo This script will do the following things for you:
echo:
echo   1. Download the git repository to your desktop via:
echo      git clone https://github.com/davidweber411/SimpleJavaFxTestBase
echo:
echo   2. Compile the project to your local maven repository via:
echo      ./gradlew publishToMavenLocal
echo:
echo The script starts after clicking any key.
echo: 
echo ====================================================================================
echo WARNING: 
echo There mustn't be a folder with the name %repositoryName% on your desktop.
echo ==================================================================================== 
echo: 
pause
echo: 

rem execute commands
:entry_point
if exist "%folderPath%\%repositoryName%" (
	cls
	color 4
	echo ====================================================================================
	echo The folder "%folderPath%\%repositoryName%" already exists. 
	echo:
	echo Please delete or move this folder and press any key again.
	echo If this does not work, exit the script and run the steps manually. 
	echo ====================================================================================
	echo:
	pause
	goto entry_point 
) else (
	cls
	color 07
	echo ====================================================================================
	echo Starting to clone the git repository
	echo ====================================================================================
	echo:
	cd %folderPath%
	rem rmdir /S /Q %folderPath%\%repositoryName%
	git clone https://github.com/davidweber411/SimpleJavaFxTestBase
	echo:
	echo ====================================================================================
	echo Executing the gradle wrapper task to publish the JAR to maven local
	echo ====================================================================================
	cd %repositoryName%
	.\gradlew publishToMavenLocal
	rem rmdir /S /Q %folderPath%\%repositoryName%
	echo ====================================================================================
	echo DONE
	echo ====================================================================================
	echo:
	echo The generated JAR should be here: 
	echo C:\Users\%username%\.m2\repository\com\wedasoft\SimpleJavaFxTestBase\...
	echo:
	echo Don't forget to delete the git repository folder on your desktop. 
	echo:
	pause
)


