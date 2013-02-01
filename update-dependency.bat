
cd %~dp0
call mvn -DdownloadSources=true -DdownloadJavadocs=true eclipse:clean eclipse:eclipse
pause
