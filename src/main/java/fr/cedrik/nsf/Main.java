/**
 *
 */
package fr.cedrik.nsf;

import java.io.IOException;

import lotus.domino.NotesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author C&eacute;drik LIME
 */
public class Main {

	protected static final Logger log = LoggerFactory.getLogger(Main.class);

	private Main() {
		assert false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, NotesException {
		// TODO graphical interface
		String notesPassword = null; // null: ask on console
		Platform platform = null; // null: auto-detect
		String emailNSFFileName = null; // null: current email nsf file (i.e. not an archive)
		if (args.length == 1) {
			emailNSFFileName = args[0];
		}
		String exportArchiveFileName = null; // null: use nsf file name
		log.info("NSF Exporter");
		ProgressListener progressListener = null;
		NSFExporter nsfExporter = new NSFExporter(progressListener);
		nsfExporter.export(notesPassword, platform, emailNSFFileName, exportArchiveFileName);
		log.info("All done!");
	}

}
