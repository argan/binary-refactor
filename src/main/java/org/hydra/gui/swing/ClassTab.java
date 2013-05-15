package org.hydra.gui.swing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.hydra.renamer.ClassInfo;

public class ClassTab extends JPanel {
	private static final long serialVersionUID = 5335853258657051179L;

	private ClassInfo classInfo;
	private ChangableTableModel fieldModel;
	private ChangableTableModel methodModel;
	private ClassInfoPanel classInfoPanel;

	public ClassTab(ClassInfo clazzInfo) {
		super(new GridLayout(1, 1));
		this.classInfo = clazzInfo;
		JScrollPane pane = new JScrollPane();
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(pane);
		JPanel panels = new JPanel();
		pane.setViewportView(panels);

		panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));

		this.classInfoPanel = new ClassInfoPanel(this.classInfo);
		panels.add(this.classInfoPanel);

		if (this.classInfo.getFields().size() > 0) {
			this.fieldModel = new FieldModel(this.classInfo.getFields());
			panels.add(new TablePanel(this.fieldModel, "Fields Info"));
			panels.revalidate();
		}

		if (this.classInfo.getMethods().size() > 0) {
			this.methodModel = new MethodModel(this.classInfo.getMethods());
			panels.add(new TablePanel(this.methodModel, "Methods Info"));
			panels.revalidate();
		}

		JPanel buttons = new JPanel();
		JButton apply = new JButton("Apply Changes");
		buttons.add(apply);
		JButton reset = new JButton("Reset Changes");
		buttons.add(reset);

		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyChanges();
			}
		});
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetChanges();
			}
		});
		panels.add(buttons);
		revalidate();
	}

	protected void resetChanges() {
		this.classInfoPanel.resetChanges();
		this.fieldModel.resetChanges();
		this.methodModel.resetChanges();
	}

	protected void applyChanges() {
		String renameScript = this.classInfoPanel.getChangeScript() + this.fieldModel.getChangeScript()
				+ this.methodModel.getChangeScript();
		System.out.println(renameScript);
	}

	public String getTitle() {
		return this.classInfo.getClassShortName();
	}
}
