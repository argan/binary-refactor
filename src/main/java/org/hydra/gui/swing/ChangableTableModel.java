package org.hydra.gui.swing;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import org.hydra.gui.swing.MethodModel.Point;
import org.hydra.util.Lists;
import org.hydra.util.Log;
import org.hydra.util.Lists.Pair;

abstract class ChangableTableModel extends DefaultTableModel {
    private static final long                        serialVersionUID = -6633055833724585182L;
    // 保存修改的内容，保留最原始的值和最新修改的值
    protected Map<Point, Lists.Pair<String, String>> changeSet        = new HashMap<Point, Lists.Pair<String, String>>();

    public ChangableTableModel(String[] title, int size) {
        super(title, size);
    }

    public void resetChanges() {
        this.changeSet.clear();
    }

    public abstract String getChangeScript();

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue == null || aValue.toString().trim().length() == 0) {
            return;
        }
        String oldValue = getValueAt(row, column).toString();
        Point p = new Point(row, column);
        Pair<String, String> pair = null;
        Log.debug("set value at %s to %s", p, aValue);

        if (this.changeSet.get(p) != null) {
            pair = new Pair<String, String>(this.changeSet.get(p).getLeft(), aValue.toString());
        } else {
            pair = new Pair<String, String>(oldValue, aValue.toString());
        }
        if (!pair.getLeft().equals(pair.getRight())) {
            this.changeSet.put(p, pair);
        } else {
            // 相当于没改
            this.changeSet.remove(p);
        }
    }

    protected String getChangedValue(int row, int col) {
        Point p = new Point(row, col);
        if (this.changeSet.get(p) != null) {
            return this.changeSet.get(p).getRight();
        }
        return null;
    }
}
