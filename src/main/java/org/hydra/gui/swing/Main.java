package org.hydra.gui.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main extends JFrame {
	private JFileChooser fileChooser = new JFileChooser();
	private JTabbedPane tabbedPane;
	private List<File> openFiles;

	public Main() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 600);
		this.fileChooser.setFileFilter(new FileNameExtensionFilter("Java Binary Files(*.jar,*.class,*.zip)",
				new String[] { "jar", "class", "zip" }));
		this.openFiles = new ArrayList<File>();

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

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

	}

	protected void openFile() {
		int status = this.fileChooser.showOpenDialog(this);
		if (status == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			int index = this.openFiles.indexOf(selectedFile);
			if (index != -1) {
				// 已经打开了，焦点切过去
				this.tabbedPane.setSelectedIndex(index);
			} else {
				index = this.tabbedPane.getTabCount();
				JarTab tab = new JarTab(selectedFile);
				this.openFiles.add(selectedFile);
				this.tabbedPane.insertTab(tab.getName(), null, tab, null, index);
				this.tabbedPane.setSelectedIndex(index);
			}
		} else if (status == JFileChooser.CANCEL_OPTION) {
			System.out.println(JFileChooser.CANCEL_OPTION);
		}
	}

	public static void main(String[] args) {
		new Main().setVisible(true);
	}
}
