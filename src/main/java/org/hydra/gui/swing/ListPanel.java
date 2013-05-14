package org.hydra.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class ListPanel extends JPanel {

	private static final long serialVersionUID = 4928767118527343566L;

	public ListPanel(AbstractTableModel model, String name) {

		JTable table = new JTable();
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setBackground(Color.LIGHT_GRAY);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(name));
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.revalidate();
	}
}
