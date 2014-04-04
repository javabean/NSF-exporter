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

public class Frame extends JFrame {

	/**  */
	private static final long serialVersionUID = 411013572272625708L;

	public Frame(){
		setTitle(Constants.FRAME_TITLE); 
		setSize(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT); 
		setLocationRelativeTo(null); 
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(buildContentPane());
	}

	private JPanel buildContentPane() {
		final JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		JTextArea welcomeLabel = new JTextArea(Constants.WELCOME);
		welcomeLabel.setMaximumSize(new Dimension(700, 30));
		welcomeLabel.setMinimumSize(new Dimension(680, 30));
		welcomeLabel.setLineWrap(true);
		welcomeLabel.setWrapStyleWord(true);
		welcomeLabel.setEditable(false);
		welcomeLabel.setOpaque(false);
		Font font = welcomeLabel.getFont();  
		welcomeLabel.setFont(font.deriveFont(Font.BOLD));
		GridBagConstraints wlGbc = buildGbc(0, 0, 3, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, 20, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL);

		JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
		GridBagConstraints sGbc = buildGbc(0, 1, 3, new Insets(Constants.INSET_TOP, 50, Constants.INSET_BOTTOM, 50), GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);

		JLabel fileTypeExportlabel = new JLabel(Constants.TEXT1);
		GridBagConstraints felGbc = buildGbc(0, 2, 3, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, 5, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);

		final JTextField archiveTextField = new JTextField();
		archiveTextField.setMinimumSize(new Dimension(250, 20));
		archiveTextField.setColumns(50);
		archiveTextField.setEnabled(false);
		GridBagConstraints atfGbc = buildGbc(1, 4, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.LINE_START, 0);

		final JFileChooser archiveFileChooser = new JFileChooser();
		final JButton archiveButton = new JButton(Constants.ACTION_PICKFILE);
		archiveButton.setEnabled(false);
		archiveButton.addActionListener(getFileOrDirectory(archiveFileChooser, archiveTextField));
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
		lotusPasswordTextField.setColumns(50);
		GridBagConstraints lptfGbc = buildGbc(1, 5, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.LINE_START, 0);
	
		JProgressBar progressBar = new JProgressBar();
		GridBagConstraints pbgbc = buildGbc(0, 9, 3, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), 0, GridBagConstraints.HORIZONTAL);
		
		JLabel exportPlaceLabel = new JLabel(Constants.TEXT3);
		GridBagConstraints eplGbc = buildGbc(0, 7, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);

		final JTextField exportPlaceTextField = new JTextField();
		exportPlaceTextField.setMinimumSize(new Dimension(250, 20));
		exportPlaceTextField.setColumns(50);
		GridBagConstraints eptfGbc = buildGbc(1, 7, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.LINE_START, 0);
		
		final JFileChooser exportPlaceFileChooser = new JFileChooser();
		exportPlaceFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final JButton exportPlaceButton = new JButton(Constants.ACTION_PICKDIRECTORY);
		exportPlaceButton.addActionListener(getFileOrDirectory(exportPlaceFileChooser, exportPlaceTextField));
		GridBagConstraints epfcGbc = buildGbc(2, 7, 0, new Insets(Constants.INSET_TOP, Constants.INSET_LEFT, Constants.INSET_BOTTOM, Constants.INSET_RIGHT), GridBagConstraints.FIRST_LINE_START, 0);
	
		final JButton buttonGo = new JButton(Constants.ACTION_GO);
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
	//TODO Final Action !
	private ActionListener finalActionListener(final JTextField lotusPasswordTextField, final JTextField archiveTextField, final JTextField exportPlaceTextField) {
		ActionListener actionlistener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Password : " + lotusPasswordTextField.getText());
				System.out.println("Archive : " + archiveTextField.getText());
				System.out.println("Export Place : " + exportPlaceTextField.getText());
				System.out.println("A toi de jouer !");
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
	private ActionListener getFileOrDirectory(final JFileChooser fileChooser, final JTextField textField) {
		ActionListener actionlistener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(Frame.this);
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