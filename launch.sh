#/bin/sh

zip -qd lib/iNotes-exporter-1.*.jar META-INF/INDEX.LIST simplelogger.properties

export NOTES_PATH=/Applications/Lotus\ Notes.app/Contents/MacOS
#export PATH=$PATH:"$NOTES_PATH"
#export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:"$NOTES_PATH"
export CLASSPATH="lib/*":$NOTES_PATH:"$NOTES_PATH/jvm/lib/ext/Notes.jar":"target/*"
export DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:"$NOTES_PATH"
java -d32 -Xmx256M -Djava.library.path="$NOTES_PATH" -cp "$CLASSPATH" fr.cedrik.nsf.Main $*

