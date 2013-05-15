package org.hydra.gui.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import org.hydra.renamer.FieldInfo;

class FieldModel extends DefaultTableModel {
	private static final long serialVersionUID = -5575263948964070610L;
	private static String[] title = new String[] { "Flags", "Type", "Name", "Default" };
	private List<FieldInfo> fields;

	public FieldModel(Set<FieldInfo> info) {
		super(title, info.size());
		this.fields = new ArrayList<FieldInfo>(info);
		Collections.sort(this.fields);
		this.fireTableStructureChanged();
		this.fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String value = "";
		FieldInfo field = this.fields.get(rowIndex);
		switch (columnIndex) {
		case 0:
			value = field.getFlagString();
			break;
		case 1:
			value = field.getType();
			break;
		case 2:
			value = field.getName();
			break;
		case 3:
		default:

		}
		return value;
	}
}