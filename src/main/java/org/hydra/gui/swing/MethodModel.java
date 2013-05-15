package org.hydra.gui.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import org.hydra.renamer.MethodInfo;
import org.hydra.util.Lists;

class MethodModel extends DefaultTableModel {
	private static final long serialVersionUID = -5575263948964070610L;
	private static String[] title = new String[] { "Flags", "Return Type", "Name", "Parameters", "Exceptions" };
	private List<MethodInfo> methods;

	public MethodModel(Set<MethodInfo> info) {
		super(title, info.size());
		this.methods = new ArrayList<MethodInfo>(info);
		Collections.sort(this.methods);
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
		MethodInfo method = this.methods.get(rowIndex);
		switch (columnIndex) {
		case 0:
			value = method.getFlagString();
			break;
		case 1:
			value = method.getReturnType();
			break;
		case 2:
			value = method.getName();
			break;
		case 3:
			value = "(" + Lists.mkString(method.getParameterTypes(), ";") + ")";
			break;
		case 4:
			value = Lists.mkString(method.getExceptions());
			break;
		default:
			value = "";
		}
		return value;
	}
}