package org.hydra.gui.swing;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class ClassTab {
	private JTabbedPane classTab;
	private String className;

	public ClassTab(String fullClassName, JTabbedPane tabbedPane) {
		this.classTab = tabbedPane;
		this.className = fullClassName;
		JScrollPane pane = new JScrollPane();
		// this.classTab.addTab("New tab", null, scrollPane_1, null);
		pane.add(new JLabel(this.className));
		UIHelper.addTab(this.className, this.classTab, pane);
	}
}
