package org.hydra.gui.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

public class ClosableTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 2794838419138219195L;

	public interface TabCreator {
		public String getName();

		public Component getTabComponent();
	}

	private List<Object> openedTabObjs = new ArrayList<Object>();

	public ClosableTabbedPane(int position) {
		super(position);
	}

	public void addOrSelectTab(Object obj, TabCreator creator) {
		int index = this.openedTabObjs.indexOf(obj);
		if (index != -1) {
			// 已经打开了，焦点切过去
			this.setSelectedIndex(index);
		} else {
			this.openedTabObjs.add(obj);
			this.addTab(creator.getName(), creator.getTabComponent());
			this.setSelectedIndex(this.getTabCount() - 1);
		}
	}

	@Override
	public void remove(int index) {
		super.remove(index);
		openedTabObjs.remove(index);
	}

	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		this.setTabComponentAt(index, new ButtonTabComponent(this));
	}

	private class ButtonTabComponent extends JPanel {
		private static final long serialVersionUID = -4291330891589529687L;
		private final JTabbedPane pane;

		public ButtonTabComponent(final JTabbedPane pane) {
			// unset default FlowLayout' gaps
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));
			if (pane == null) {
				throw new NullPointerException("TabbedPane is null");
			}
			this.pane = pane;
			setOpaque(false);

			// make JLabel read titles from JTabbedPane
			JLabel label = new JLabel() {
				private static final long serialVersionUID = -3992263302324567498L;

				public String getText() {
					int i = pane.indexOfTabComponent(ButtonTabComponent.this);
					if (i != -1) {
						return pane.getTitleAt(i);
					}
					return null;
				}
			};

			add(label);
			// add more space between the label and the button
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			// tab button
			JButton button = new TabButton();
			add(button);
			// add more space to the top of the component
			setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		}

		private class TabButton extends JButton implements ActionListener {
			private static final long serialVersionUID = 8332157928000059927L;

			public TabButton() {
				int size = 17;
				setPreferredSize(new Dimension(size, size));
				setToolTipText("close this tab");
				// Make the button looks the same for all Laf's
				setUI(new BasicButtonUI());
				// Make it transparent
				setContentAreaFilled(false);
				// No need to be focusable
				setFocusable(false);
				setBorder(BorderFactory.createEtchedBorder());
				setBorderPainted(false);
				// Making nice rollover effect
				// we use the same listener for all buttons
				addMouseListener(buttonMouseListener);
				setRolloverEnabled(true);
				// Close the proper tab by clicking the button
				addActionListener(this);
			}

			public void actionPerformed(ActionEvent e) {
				int i = pane.indexOfTabComponent(ButtonTabComponent.this);
				if (i != -1) {
					pane.remove(i);
				}
			}

			// we don't want to update UI for this button
			public void updateUI() {
			}

			// paint the cross
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				// shift the image for pressed buttons
				if (getModel().isPressed()) {
					g2.translate(1, 1);
				}
				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.BLACK);
				if (getModel().isRollover()) {
					g2.setColor(Color.MAGENTA);
				}
				int delta = 6;
				g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
				g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
				g2.dispose();
			}
		}

		private final MouseListener buttonMouseListener = new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				Component component = e.getComponent();
				if (component instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) component;
					button.setBorderPainted(true);
				}
			}

			public void mouseExited(MouseEvent e) {
				Component component = e.getComponent();
				if (component instanceof AbstractButton) {
					AbstractButton button = (AbstractButton) component;
					button.setBorderPainted(false);
				}
			}
		};
	}
}
