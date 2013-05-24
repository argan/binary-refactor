package org.hydra.gui.swing;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
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

import org.hydra.gui.swing.ClosableTabbedPane.TabCreator;
import org.hydra.renamer.ClassInfo;
import org.hydra.renamer.ClassMap;

public class JarTab extends JSplitPane {
	private static final long serialVersionUID = 5862833774145303335L;
	private File jarFile;
	private JTree tree;
	private ClassMap classMap;

	public JarTab(File jarFile) {
		this.jarFile = jarFile;

		JScrollPane scrollPane = new JScrollPane();
		this.setLeftComponent(scrollPane);
		final ClosableTabbedPane classTabPane = new ClosableTabbedPane(JTabbedPane.TOP);
		this.setRightComponent(classTabPane);

		tree = new JTree(parseFile());
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				selectClass(classTabPane);
			}
		});
		scrollPane.setViewportView(tree);

	}

	protected String getFullClassName(TreeNode treeNode) {
		String pkg = treeNode.getParent().toString();
		return pkg + "." + treeNode.toString();
	}

	private DefaultMutableTreeNode parseFile() {
		// parse file

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(this.getName());
		try {
			classMap = ClassMap.build(new JarFile(this.jarFile));

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

	private void selectClass(final ClosableTabbedPane classTabPane) {
		final TreeNode treeNode = (TreeNode) tree.getLastSelectedPathComponent();
		if (treeNode.isLeaf()) {
			final String fullClassName = getFullClassName(treeNode);
			classTabPane.addOrSelectTab(fullClassName, new TabCreator() {

				@Override
				public String getName() {
					return treeNode.toString();
				}

				@Override
				public Component getTabComponent() {
					// TODO Auto-generated method stub
					return new ClassTab(JarTab.this.classMap.getClassInfo(fullClassName));
				}
			});
		}
	}

}
