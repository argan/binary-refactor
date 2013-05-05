package org.hydra.gui.swing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarFile;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.hydra.renamer.ClassInfo;
import org.hydra.renamer.ClassMap;
import org.hydra.renamer.RenameConfig;

public class JarTab extends JSplitPane {
	private File jarFile;
	private JTree tree;
	private List<String> openClasses;

	public JarTab(File jarFile) {
		this.jarFile = jarFile;
		this.openClasses = new ArrayList<String>();

		JScrollPane scrollPane = new JScrollPane();
		this.setLeftComponent(scrollPane);
		final JTabbedPane classTabPane = new JTabbedPane(JTabbedPane.TOP);
		this.setRightComponent(classTabPane);

		tree = new JTree(parseFile());
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreeNode treeNode = (TreeNode) tree.getLastSelectedPathComponent();
				if (treeNode.isLeaf()) {
					String fullClassName = getFullClassName(treeNode);
					ClassTab classTab = new ClassTab(fullClassName, classTabPane);
				}
				// JOptionPane.showMessageDialog(null, treeNode.getParent(),
				// "错误", JOptionPane.ERROR_MESSAGE);
			}
		});
		scrollPane.setViewportView(tree);

	}

	protected String getFullClassName(TreeNode treeNode) {
		return treeNode.toString();
	}

	private DefaultMutableTreeNode parseFile() {
		// parse file
		ClassMap classMap;
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this.getName());
		try {
			classMap = ClassMap.build(new JarFile(this.jarFile));
			classMap.rebuildConfig(new RenameConfig(), null);

			for (Map.Entry<String, List<ClassInfo>> entry : classMap.getTree().entrySet()) {
				node.add(buildNode(entry));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return node;
	}

	private MutableTreeNode buildNode(Entry<String, List<ClassInfo>> entry) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(entry.getKey().replace('/', '.'));
		for (ClassInfo clazz : entry.getValue()) {
			node.add(new DefaultMutableTreeNode(clazz.getClassShortName()));
		}
		return node;
	}

	public String getName() {
		return this.jarFile.getName();
	}

}
