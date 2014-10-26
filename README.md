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
	mvn -Dmaven.test.skip clean assembly:single

Running
-------

**First quit Lotus Notes!**

Use the `.tool` (Macintosh) or `.cmd` (Windows) GUI launcher

By default, archive files are  
`C:\Users\<you>\AppData\Local\Lotus\Notes\Data\archive\a_[name].nsf`  
or   
`/Users/<you>/Library/Application Support/Lotus Notes Data/archive/a_[name].nsf`.  
In particular, do *not* select the `l_[name].nsf` file as it is *not* an archive!

If you stop the process for any reason, be sure to terminate the associated `java` process in Activity Manager / Task Manager (this is a Lotus Notes limitation)!

Credits
-------

GUI code based on contribution by Émilie CHIROL. Thanks Émilie!

A big thank you too to Thierry Bur who is a fantastic beta-tester!