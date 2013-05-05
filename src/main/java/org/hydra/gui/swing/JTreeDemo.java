package org.hydra.gui.swing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class JTreeDemo extends JPanel implements Runnable {

	private JTree tree;
	private DefaultTreeModel treeModel;
	private Random rnd = new Random();
	private List<User> userList;

	public JTreeDemo() {
		super();

		// Create the nodes.
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Users");
		treeModel = new DefaultTreeModel(top);
		createNodes(top);

		// Create a tree that allows one selection at a time.
		tree = new JTree(treeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		// Create the scroll pane and add the tree to it.
		JScrollPane treeView = new JScrollPane(tree);

		// Add the split pane to this panel.
		add(treeView);
	}

	public String getRandomStatus() {
		int nextInt = rnd.nextInt(100);
		if (nextInt % 2 == 0) {
			return "Online";
		} else {
			return "Offline";
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				int nextInt = rnd.nextInt(10);
				User user = userList.get(nextInt);
				user.setStatus(getRandomStatus());
				treeModel.nodeChanged(user);
			} catch (InterruptedException ex) {
				// handle it if necessary
			}
		}
	}

	private class User extends DefaultMutableTreeNode {
		public String userName;
		public String status;

		public User(String name) {
			userName = name;

		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

		@Override
		public String toString() {
			String color = status.equals("Online") ? "Green" : "Red";
			return "<html><b color='" + color + "'>" + userName + "-" + status + "</b></html>";
		}

	}

	private void createNodes(DefaultMutableTreeNode top) {
		userList = new ArrayList();
		for (int i = 0; i < 10; i++) {
			User u1 = new User("User " + (i + 1));
			u1.setStatus("Online");
			top.add(u1);
			userList.add(u1);
		}
	}

	private static void createAndShowGUI() {

		JFrame frame = new JFrame("TreeDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Add content to the window.
		JTreeDemo jTreeDemo = new JTreeDemo();
		frame.add(jTreeDemo);
		frame.pack();
		frame.setVisible(true);
		// update status randomly
		Thread thread = new Thread(jTreeDemo);
		thread.start();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}