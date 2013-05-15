package org.hydra.gui.swing;

import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MultiClassNamePanel extends JPanel {
	private static final long serialVersionUID = -8606214971927866480L;

	public MultiClassNamePanel(Collection<String> names) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		for (String name : names) {
			add(new JButton(name));
		}
	}
}
