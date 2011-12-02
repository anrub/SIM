package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * Panel to send messages.
 * 
 * @author flo
 * 
 */
public class SendReceiveMessagePanel extends JPanel {

	private JTabbedPane tabbedPane;

	public SendReceiveMessagePanel() {
		super();
		setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

		addToTabPane("Info");

		// Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();

		buttonPane.add(new JButton("Send"));
		buttonPane.add(new JButton("Cancel"));

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Component c = tabbedPane.getSelectedComponent();
				String name = c.getName();
				tabbedPane.remove(c);
				
				
			}
		});
		buttonPane.add(closeButton);

		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.PAGE_END);
	}

	/**
	 * Fuegt ein SendReceiveMessage Tab fuer diese Konversation ein.
	 * 
	 * @param label
	 *            Label des Tabs
	 */
	public void addToTabPane(String label) {
		JPanel textPanel = new JPanel(new BorderLayout());

		JTextArea timelineTextArea = new JTextArea(5, 50);
		timelineTextArea.setWrapStyleWord(true);
		timelineTextArea.setLineWrap(true);
		timelineTextArea.setEditable(false);
		JScrollPane textScrollPane = new JScrollPane(timelineTextArea);
		textPanel.add(textScrollPane, BorderLayout.CENTER);

		JTextArea messageTextArea = new JTextArea(2, 50);
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setLineWrap(true);
		JScrollPane messageScrollPane = new JScrollPane(messageTextArea);

		textPanel.add(messageScrollPane, BorderLayout.SOUTH);

		tabbedPane.addTab(label, textPanel);

	}

	/**
	 * fokussiert das tabpane mit dem Namen.
	 * 
	 * @param name
	 *            Name des Panes.
	 */
	public void focusTabPane(String name) {
		int index = tabbedPane.indexOfTab(name);
		if (index != -1) {
			tabbedPane.setSelectedIndex(index);
		}
	}

}
