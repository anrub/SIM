package devhood.im.sim.ui;

import java.awt.Dimension;

import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * In diesem Frame koennen verschiedene Konfigs vorgenommen werden.
 * 
 * @author flo
 * 
 */
@Named("configurationFrame")
public class ConfigurationFrame extends JFrame {

	public ConfigurationFrame() {
		setTitle("Einstellungen");
		setMinimumSize(new Dimension(500, 400));

		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Farben");
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
				"Benachrichtigungen");
		DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(
				"Look & Feel");
		DefaultMutableTreeNode node4 = new DefaultMutableTreeNode(
				"About");
		DefaultMutableTreeNode node5 = new DefaultMutableTreeNode(
				"Sonstiges");
		
		JTree configItemsTree = new JTree(new Object[] { node1, node2, node3, node4, node5 });

		JPanel pane1 = new JPanel();
		JLabel label1 = new JLabel("Konfig test");
		pane1.add(label1);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		splitPane.add(configItemsTree);
		splitPane.add(pane1);

		add(splitPane);

		// pack();
		// setVisible(true);
	}

}
