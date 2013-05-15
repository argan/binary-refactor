package org.hydra.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.hydra.gui.swing.ClosableTabbedPane.TabCreator;
import org.hydra.util.Log;

public class Main extends JFrame {
	private static final long serialVersionUID = -9210020227316296003L;
	private JFileChooser fileChooser = new JFileChooser();
	private ClosableTabbedPane tabbedPane;

	public Main() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		this.fileChooser.setFileFilter(new FileNameExtensionFilter("Java Binary Files(*.jar,*.class,*.zip)",
				new String[] { "jar", "class", "zip" }));

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmOpen = new JMenuItem("Open File ...");
		mnNewMenu.add(mntmOpen);
		mntmOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		JMenuItem mntmClose = new JMenuItem("Close");
		mnNewMenu.add(mntmClose);

		JSeparator separator = new JSeparator();
		mnNewMenu.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnNewMenu.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.this.dispose();
			}
		});

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		JMenu mnNavigate = new JMenu("Navigate");
		menuBar.add(mnNavigate);

		JMenu mnSearch = new JMenu("Search");
		menuBar.add(mnSearch);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);

		tabbedPane = new ClosableTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

	}

	protected void openFile() {
		int status = this.fileChooser.showOpenDialog(this);
		if (status == JFileChooser.APPROVE_OPTION) {
			final File selectedFile = fileChooser.getSelectedFile();
			this.tabbedPane.addOrSelectTab(selectedFile, new TabCreator() {

				@Override
				public String getName() {
					return selectedFile.getName();
				}

				@Override
				public Component getTabComponent() {
					return new JarTab(selectedFile);
				}
			});
		} else if (status == JFileChooser.CANCEL_OPTION) {
			Log.debug("Cancel file choose %d",JFileChooser.CANCEL_OPTION);
		}
	}

	public static void main(String[] args) {
		new Main().setVisible(true);
	}
}
