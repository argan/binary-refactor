package org.hydra.gui.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hydra.gui.swing.MethodModel.Point;
import org.hydra.renamer.FieldInfo;
import org.hydra.util.Lists;

class FieldModel extends ChangableTableModel {
    private static final long serialVersionUID = -5575263948964070610L;
    private static String[]   title            = new String[] { "Flags", "Type", "Name", "Value" };
    private List<FieldInfo>   fields;

    public FieldModel(Set<FieldInfo> info) {
        super(title, info.size());
        this.fields = new ArrayList<FieldInfo>(info);
        Collections.sort(this.fields);
        this.fireTableStructureChanged();
        this.fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2 && !"serialVersionUID".equals(getValueAt(row, column));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (super.getChangedValue(rowIndex, columnIndex) != null) {
            return super.getChangedValue(rowIndex, columnIndex);
        }
        Object value = "";
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
                value = field.getValue();
                break;
            default:

        }
        return value;
    }

    @Override
    public String getChangeScript() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Point, Lists.Pair<String, String>> entry : changeSet.entrySet()) {
            FieldInfo field = this.fields.get(entry.getKey().getRow());
            sb.append(String.format("field: %s %s to %s\n", entry.getValue().getLeft(),
                field.getDesc(), entry.getValue().getRight()));
        }
        return sb.toString();
    }
}