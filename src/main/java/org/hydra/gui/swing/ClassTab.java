package org.hydra.gui.swing;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.hydra.renamer.ClassInfo;

public class ClassTab extends JPanel {
	private static final long serialVersionUID = 5335853258657051179L;

	private ClassInfo classInfo;

	public ClassTab(ClassInfo clazzInfo) {
		super(new GridLayout(1, 1));
		this.classInfo = clazzInfo;
		JScrollPane pane = new JScrollPane();
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(pane);
		JPanel panels = new JPanel();
		pane.setViewportView(panels);

		panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));

		panels.add(new ClassInfoPanel(this.classInfo));

		if (this.classInfo.getFields().size() > 0) {
			panels.add(new ListPanel(new FieldModel(this.classInfo.getFields()), "Fields Info"));
			panels.revalidate();
		}

		if (this.classInfo.getMethods().size() > 0) {
			panels.add(new ListPanel(new MethodModel(this.classInfo.getMethods()), "Methods Info"));
			panels.revalidate();
		}

		revalidate();
	}

	public String getTitle() {
		return this.classInfo.getClassShortName();
	}
}
