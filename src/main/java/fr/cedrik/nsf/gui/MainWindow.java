package fr.cedrik.nsf.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import fr.cedrik.nsf.Platform;

/**
 * @author &Eacute;milie CHIROL
 * @author C&eacute;drik LIME
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	protected JProgressBar progressBar;
	protected JButton buttonGo;

	public MainWindow() {
		setTitle(Constants.FRAME_TITLE);
		setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setContentPane(buildContentPane());
	}

	private JPanel buildContentPane() {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JTextArea welcomeLabel = new JTextArea(Constants.WELCOME);
		welcomeLabel.setMaximumSize(new Dimension(700, 48));
		welcomeLabel.setMinimumSize(new Dimension(680, 48));
		welcomeLabel.setLineWrap(true);
		welcomeLabel.setWrapStyleWord(true);
		welcomeLabel.setEditable(false);
		welcomeLabel.setOpaque(false);
		Font font = welcomeLabel.getFont();
		welcomeLabel.setFont(font.deriveFont(Font.BOLD));
		GridBagConstraints wlGbc = buildGbc(0, 0, 3, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, 20, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL);

		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		GridBagConstraints sGbc = buildGbc(0, 1, 3, new Insets(Constants.INSET_TOP, 50, Constants.INSET_BOTTOM, 50), GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

		JLabel fileTypeExportlabel = new JLabel(Constants.TEXT1);
		GridBagConstraints felGbc = buildGbc(0, 2, 3, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, 5, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);

		final JTextField archiveTextField = new JTextField();
		archiveTextField.setMinimumSize(new Dimension(250, 20));
		archiveTextField.setColumns(70);
		archiveTextField.setEnabled(false);
		GridBagConstraints atfGbc = buildGbc(1, 4, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.LINE_START, 0);

		final JFileChooser archiveFileChooser = new JFileChooser();
		archiveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//		archiveFileChooser.setFileHidingEnabled(false);
		archiveFileChooser.setFileFilter(new FileNameExtensionFilter("Lotus Notes archive", "nsf"));
		{
			File archiveDirectory = new File(SystemUtils.getUserHome(), "Library/Application Support/Lotus Notes Data/archive/");
			if (! archiveDirectory.exists()) {
				archiveDirectory = new File(SystemUtils.getUserHome(), "AppData/Local/Lotus/Notes/Data/archive/");
			}
			if (! archiveDirectory.exists()) {
				archiveDirectory = null;
			}
			archiveFileChooser.setCurrentDirectory(archiveDirectory);
		}
		final JButton archiveButton = new JButton(Constants.ACTION_PICKFILE);
		archiveButton.setEnabled(false);
		archiveButton.addActionListener(getFileOrDirectory(archiveFileChooser, true, archiveTextField));
		GridBagConstraints abGbc = buildGbc(2, 4, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);

		final JRadioButton notArchivedRb = new JRadioButton(Constants.OPT1);
		notArchivedRb.setSelected(true);
		final JRadioButton archivedRb = new JRadioButton(Constants.OPT2);

		notArchivedRb.addActionListener(updateRadioButtons(notArchivedRb, archivedRb, archiveTextField, archiveButton));
		archivedRb.addActionListener(updateRadioButtons(notArchivedRb, archivedRb, archiveTextField, archiveButton));

		GridBagConstraints narbGbc = buildGbc(0, 3, 2, new Insets(Constants.INSET_TOP, 20, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);
		GridBagConstraints arbGbc = buildGbc(0, 4, 0, new Insets(Constants.INSET_TOP, 20, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);

		ButtonGroup group = new ButtonGroup();
		group.add(notArchivedRb);
		group.add(archivedRb);

		JLabel lotusPasswordLabel = new JLabel(Constants.TEXT2);
		GridBagConstraints lplGbc = buildGbc(0, 5, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);

		final JTextField lotusPasswordTextField = new JPasswordField();
		lotusPasswordTextField.setMinimumSize(new Dimension(250, 20));
		lotusPasswordTextField.setColumns(70);
		GridBagConstraints lptfGbc = buildGbc(1, 5, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.LINE_START, 0);

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		GridBagConstraints pbgbc = buildGbc(0, 9, 3, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), 0, GridBagConstraints.HORIZONTAL);

		JLabel exportPlaceLabel = new JLabel(Constants.TEXT3);
		GridBagConstraints eplGbc = buildGbc(0, 7, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);

		final JTextField exportPlaceTextField = new JTextField();
		exportPlaceTextField.setMinimumSize(new Dimension(250, 20));
		exportPlaceTextField.setColumns(70);
		GridBagConstraints eptfGbc = buildGbc(1, 7, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.LINE_START, 0);

		final JFileChooser exportPlaceFileChooser = new JFileChooser();
		exportPlaceFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		exportPlaceFileChooser.setCurrentDirectory(SystemUtils.getUserDir());
		final JButton exportPlaceButton = new JButton(Constants.ACTION_PICKDIRECTORY);
		exportPlaceTextField.setText(exportPlaceFileChooser.getCurrentDirectory().getAbsolutePath());
		exportPlaceButton.addActionListener(getFileOrDirectory(exportPlaceFileChooser, false, exportPlaceTextField));
		GridBagConstraints epfcGbc = buildGbc(2, 7, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);

		buttonGo = new JButton(Constants.ACTION_GO);
		GridBagConstraints gbcButton3 = buildGbc(0, 8, 3, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.CENTER, 0);
		buttonGo.addActionListener(finalActionListener(lotusPasswordTextField, archiveTextField, exportPlaceTextField));

		panel.add(welcomeLabel, wlGbc);
		panel.add(separator, sGbc);
		panel.add(fileTypeExportlabel, felGbc);
		panel.add(notArchivedRb, narbGbc);
		panel.add(archivedRb, arbGbc);
		panel.add(archiveTextField, atfGbc);
		panel.add(archiveButton, abGbc);
		panel.add(lotusPasswordLabel, lplGbc);
		panel.add(lotusPasswordTextField, lptfGbc);
		panel.add(exportPlaceLabel, eplGbc);
		panel.add(exportPlaceTextField, eptfGbc);
		panel.add(exportPlaceButton, epfcGbc);
		panel.add(buttonGo, gbcButton3);
		panel.add(progressBar, pbgbc);

		return panel;
	}

	/**
	 * Final action
	 * @param lotusPasswordTextField
	 * @param archiveTextField
	 * @param exportPlaceTextField
	 * @return the final action !
	 */
	private ActionListener finalActionListener(final JTextField lotusPasswordTextField, final JTextField archiveTextField, final JTextField exportPlaceTextField) {
		ActionListener actionlistener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String notesPassword = StringUtils.isEmpty(lotusPasswordTextField.getText()) ? null : lotusPasswordTextField.getText(); // null: ask on console
				Platform platform = null; // null: auto-detect
				String emailNSFFileName = ( ! archiveTextField.isEnabled() || StringUtils.isEmpty(archiveTextField.getText())) ? null : archiveTextField.getText(); // null: current email nsf file (i.e. not an archive)
				String exportArchiveFileName = StringUtils.isEmpty(exportPlaceTextField.getText()) ? null : exportPlaceTextField.getText(); // null: use nsf file name

				SwingWorker<Void, Void> worker = new NSFExportWorker(notesPassword, platform, emailNSFFileName, exportArchiveFileName, new Runnable() {
					@Override
					public void run() {
						setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
						buttonGo.setEnabled(false);
						progressBar.setValue(progressBar.getMinimum());
						progressBar.setIndeterminate(true);
					}
				}, new Runnable() {
					@Override
					public void run() {
						progressBar.setValue(progressBar.getMaximum());
						progressBar.setIndeterminate(false);
						buttonGo.setEnabled(true);
						setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
					}
				});
				worker.execute();
//				worker.get();// blocks GUI!
			}
		};
		return actionlistener;
	}

	/**
	 * Allow to open a file chooser and get a file or directory.
	 * @param fileChooser
	 * @param textField
	 * @return set the file picked in a textfield
	 */
	private ActionListener getFileOrDirectory(final JFileChooser fileChooser, final boolean open, final JTextField textField) {
		ActionListener actionlistener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = open ? fileChooser.showOpenDialog(MainWindow.this) : fileChooser.showSaveDialog(MainWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					textField.setText(file.getAbsolutePath());
				}
			}
		};
		return actionlistener;
	}

	/**
	 * Update IHM
	 * @param radioButton1
	 * @param radioButton2
	 * @param textField
	 * @param jButton
	 * @return enabled or not some field
	 */
	private ActionListener updateRadioButtons(final JRadioButton radioButton1, final JRadioButton radioButton2, final JTextField textField, final JButton jButton) {
		ActionListener actionlistener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object source = e.getSource();
				if (source == radioButton1) {
					textField.setEnabled(false);
					jButton.setEnabled(false);
				} else if(source == radioButton2) {
					textField.setEnabled(true);
					jButton.setEnabled(true);
					jButton.doClick();
				}
			}
		};
		return actionlistener;
	}

	private GridBagConstraints buildGbc(int gridX, int gridY, int gridWidth, Insets insets, int anchor, int fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridX;
		gbc.gridy = gridY;
		gbc.insets = insets;

		if (gridWidth > 0) {
			gbc.gridwidth = gridWidth;
		}

		if (anchor > 0) {
			gbc.anchor = anchor;
		}

		if (fill > 0) {
			gbc.fill = fill;
		}
		return gbc;
	}
}