#/bin/sh
# Local Variables:
# coding: iso-8859-15-unix
# End:

#cd "`dirname \"$0\"`"
cd "$(dirname "$0")"

zip -qd lib/iNotes-exporter-1.*.jar META-INF/INDEX.LIST simplelogger.properties

# Shared libraries path
# The library search path environment variable name differs between different systems. On Linux it is called LD_LIBRARY_PATH, while on Mac OS X it is DYLD_LIBRARY_PATH. On Windows, it is the PATH variable.

if [ -d /Applications/Lotus\ Notes.app/Contents/MacOS ]; then
	NOTES_PATH=/Applications/Lotus\ Notes.app/Contents/MacOS
elif [ -d /Applications/Notes.app/Contents/MacOS ]; then
	NOTES_PATH=/Applications/Notes.app/Contents/MacOS
else
	echo "Can not find Lotus Notes.app!"
	exit -1
fi
export NOTES_PATH
#LD_LIBRARY_PATH=$LD_LIBRARY_PATH:"$NOTES_PATH"
#export LD_LIBRARY_PATH
DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:"$NOTES_PATH"
export DYLD_LIBRARY_PATH
CLASSPATH="lib/*":"$NOTES_PATH/jvm/lib/ext/Notes.jar":"target/*"
export CLASSPATH
java -d32 -Xmx1536M -Djava.library.path="$NOTES_PATH" -cp "$CLASSPATH" fr.cedrik.nsf.gui.Main $*
