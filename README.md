NSF-exporter
============

This project exports a Lotus Notes nsf archive as an archive which can be imported by Outlook (Macintosh and Windows versions).

Requirements
------------
* Lotus Notes (tested with version 8.5.3, should work with versions 5+ with small changes)
* Java 6 (with same architecture as Lotus Notes, i.e. 32 bits)
* Maven 3 for compiling

Compiling
---------
Compile [iNotes-exporter](https://github.com/javabean/iNotes-exporter) and place it in `lib/` (sorry, it is not available in any Maven repository!)

	zip -d lib/iNotes-exporter-*.jar META-INF/INDEX.LIST simplelogger.properties
	mvn -Dmaven.test.skip clean assembly:assembly -DdescriptorId=jar-with-dependencies

Running
-------

Use the `.sh` or `.cmd` launcher:

	launch.sh [optional .nsf archive file]

By default, archive files are in `C:\Users\<you>\AppData\Local\Lotus\Notes\Data\archive`

