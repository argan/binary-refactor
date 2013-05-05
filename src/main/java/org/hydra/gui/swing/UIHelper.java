package org.hydra.gui.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class UIHelper {

	public static void addTab(String tabName, final JTabbedPane tabPane, Component comp) {
		int index = tabPane.getTabCount();
		tabPane.insertTab(tabName, null, comp, tabName, index);
		// this.tabbedPane.addTab(this.jarFile.getName(), this.splitPane);

		JPanel pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(tabName);
		JButton btnClose = new JButton("x");
		btnClose.setSize(20, 20);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;

		pnlTab.add(lblTitle, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnClose, gbc);

		tabPane.setTabComponentAt(index, pnlTab);

		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {

				Component selected = tabPane.getSelectedComponent();
				if (selected != null) {

					tabPane.remove(selected);
					// It would probably be worthwhile getting the source
					// casting it back to a JButton and removing
					// the action handler reference ;)

				}
			}
		});
	}
}
