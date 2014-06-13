/**
 *
 */
package fr.cedrik.nsf.gui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import fr.cedrik.nsf.NSFExporter;
import fr.cedrik.nsf.Platform;

/**
 * @author &Eacute;milie CHIROL
 * @author C&eacute;drik LIME
 */
public class NSFExportWorker extends SwingWorker<Void, Void> {
	protected String notesPassword = null; // null: ask on console
	protected Platform platform = null; // null: auto-detect
	protected String emailNSFFileName = null; // null: current email nsf file (i.e. not an archive)
	protected String exportArchiveFileName = null; // null: use nsf file name

	protected NSFExportProgressMonitor progressListener;
	protected Runnable preAction, postAction;
	protected Throwable lastError;

	public NSFExportWorker(String notesPassword, Platform platform, String emailNSFFileName, String exportArchiveFileName, Runnable preAction, Runnable postAction) {
		this.notesPassword = notesPassword;
		this.platform = platform;
		this.emailNSFFileName = emailNSFFileName;
		this.exportArchiveFileName = exportArchiveFileName;
		this.preAction = preAction;
		this.postAction = postAction;
		progressListener = new NSFExportProgressMonitor(null, "Exporting messages...");
	}

	@Override
	protected Void doInBackground() throws Exception {
		if (preAction != null) {
			SwingUtilities.invokeAndWait(preAction);
		}
		NSFExporter nsfExporter = new NSFExporter(progressListener);
		try {
			nsfExporter.export(notesPassword, platform, emailNSFFileName, exportArchiveFileName);
		} catch (Throwable error) {
			lastError = error;
		}
		return null;
	}

	@Override
	protected void done() {
		progressListener.close();
		if (lastError != null) {
			// TODO Auto-generated catch block
			lastError.printStackTrace();
			JOptionPane.showMessageDialog(null, lastError.toString(), "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null,
					"All done!",
					"Export is finished", JOptionPane.INFORMATION_MESSAGE);
		}
		if (postAction != null) {
			postAction.run();
		}
		super.done();
	}
}
