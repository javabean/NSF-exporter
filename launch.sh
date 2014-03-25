#/bin/sh

zip -qd lib/iNotes-exporter-1.*.jar META-INF/INDEX.LIST simplelogger.properties

# Shared libraries path
# The library search path environment variable name differs between different systems. On Linux it is called LD_LIBRARY_PATH, while on Mac OS X it is DYLD_LIBRARY_PATH. On Windows, it is the PATH variable.

export NOTES_PATH=/Applications/Lotus\ Notes.app/Contents/MacOS
#export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:"$NOTES_PATH"
export DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:"$NOTES_PATH"
export CLASSPATH="lib/*":"$NOTES_PATH/jvm/lib/ext/Notes.jar":"target/*"
java -d32 -Xmx1024M -Djava.library.path="$NOTES_PATH" -cp "$CLASSPATH" fr.cedrik.nsf.Main $*

