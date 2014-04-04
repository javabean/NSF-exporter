package fr.cedrik.nsf.gui;

import javax.swing.SwingUtilities;

/**
 * @author &Eacute;milie CHIROL
 * @author C&eacute;drik LIME
 */
public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainWindow myFrame = new MainWindow();
				myFrame.setVisible(true);
			}
		});
	}

}
