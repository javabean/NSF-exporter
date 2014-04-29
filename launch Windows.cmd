@REM -*- coding: iso-8859-15-dos -*-
@ECHO OFF
REM zip -qd lib/iNotes-exporter-1.*.jar META-INF/INDEX.LIST simplelogger.properties
set NOTES_PATH="C:\Program Files (x86)\IBM\Lotus\Notes"
set PATH=%PATH%;%NOTES_PATH%
set CLASSPATH=lib/*;$NOTES_PATH/jvm/lib/ext/Notes.jar;target/*
%NOTES_PATH%\jvm\bin\java -Xmx1536M -Djava.library.path=%NOTES_PATH% -cp %CLASSPATH% fr.cedrik.nsf.gui.Main %*
