/**
 *
 */
package fr.cedrik.nsf.gui;

import java.awt.Component;

import javax.swing.ProgressMonitor;

import fr.cedrik.nsf.ProgressListener;

/**
 * @author C&eacute;drik LIME
 */
public class NSFExportProgressMonitor extends ProgressMonitor implements
		ProgressListener {

	protected volatile int nExportedDocs = 0;

	/**
	 * @param parentComponent
	 * @param message
	 */
	public NSFExportProgressMonitor(Component parentComponent, Object message) {
		super(parentComponent, message, "", 0, 1);
	}

	/** {@inheritDoc} */
	@Override
	public void setTotalNumberOfDocuments(int n) {
		setMaximum(n);
	}

	/** {@inheritDoc} */
	@Override
	public void documentExported(final String notesID) {
		++nExportedDocs;
		setNote(notesID);
		setProgress(nExportedDocs);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCanceled() {
		return super.isCanceled();
	}

	/** {@inheritDoc} */
	@Override
	public void close() {
		super.close();
	}
}
