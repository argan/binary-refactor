package org.hydra.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public class TablePanel extends JPanel {
	private static final long serialVersionUID = 4928767118527343566L;

	public TablePanel(AbstractTableModel model, String name) {
		JTable table = new JTable();
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setBackground(Color.LIGHT_GRAY);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());

		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(name));
		this.add(new JScrollPane(table), BorderLayout.CENTER);
		this.revalidate();

		Action action = new AbstractAction() {
			private static final long serialVersionUID = 3383004392475616535L;

			public void actionPerformed(ActionEvent e) {
				TableCellListener tcl = (TableCellListener) e.getSource();
				System.out.println("Row   : " + tcl.getRow());
				System.out.println("Column: " + tcl.getColumn());
				System.out.println("Old   : " + tcl.getOldValue());
				System.out.println("New   : " + tcl.getNewValue());
			}
		};

		new TableCellListener(table, action);
	}
}
