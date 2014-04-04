/**
 *
 */
package fr.cedrik.nsf;


/**
 * @author C&eacute;drik LIME
 * @see "http://docs.oracle.com/javase/tutorial/uiswing/components/progress.html"
 */
public interface ProgressListener {
	void setTotalNumberOfDocuments(int n);
	void documentExported(String notesID);
}
