package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

import devhood.im.sim.Sim;
import devhood.im.sim.model.Message;
import devhood.im.sim.model.User;
import devhood.im.sim.service.ServiceLocator;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.EventObserver;
import devhood.im.sim.ui.event.Events;

/**
 * Panel to send messages.
 * 
 * @author flo
 * 
 */
public class SendReceiveMessagePanel extends JPanel implements EventObserver {

	/**
	 * Tabbed pane der einzelnen Konversationen. Eine Konv. pro Tab.
	 */
	private JTabbedPane tabbedPane;

	private Map<String, JTextArea> nameTextAreaMap = new HashMap<String, JTextArea>();

	/**
	 * DateFormat.s
	 */
	private DateFormat df = new SimpleDateFormat("HH:mm:ss");

	public SendReceiveMessagePanel() {
		super();
		setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

		addToTabPane("Info", "SIM - S Instant Messenger");

		// Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel p = (JPanel) tabbedPane.getSelectedComponent();

				JTextArea timeline = (JTextArea) ((JViewport) ((JScrollPane) p
						.getComponent(0)).getComponent(0)).getComponent(0);
				JTextArea input = (JTextArea) ((JViewport) ((JScrollPane) p
						.getComponent(1)).getComponent(0)).getComponent(0);

				timeline.setText(getFormattedMessage(Sim.getUsername(),
						input.getText(), timeline.getText()));

				input.setText("");

				input.requestFocusInWindow();

				// MessagingService send message in Swing Thread ...buttonPane
				// Fehlerbehandlung
			}
		});

		buttonPane.add(sendButton);
		buttonPane.add(new JButton("Cancel"));

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = tabbedPane.getSelectedIndex();
				Component c = tabbedPane.getComponentAt(index);

				String title = tabbedPane.getTitleAt(index);
				nameTextAreaMap.remove(title);

				tabbedPane.remove(index);
			}
		});
		buttonPane.add(closeButton);

		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.PAGE_END);

		EventDispatcher.add(this);
	}

	/**
	 * Empfaengt Events.
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.USER_SELECTED.equals(event)) {
			JCheckBox box = (JCheckBox) o;
			String text = box.getText();

			int index = tabbedPane.indexOfTab(text);
			if (index == -1) {
				addToTabPane(text, null);
			}
			focusTabPane(text);
		} else if (Events.MESSAGE_RECEIVED.equals(event)) {
			if (o instanceof Message) {
				Message m = (Message) o;
				processNewMessage(m);
			} else {
				throw new IllegalArgumentException("Fuer Event "
						+ Events.MESSAGE_RECEIVED
						+ " muss eine Message als Object kkommen! ist aber: "
						+ o.getClass());
			}
		} else if (Events.SHOW_MSG_TABBED_PANE.equals(event)) {
			String name = (String) o;
			focusTabPane(name);
		}
	}

	/**
	 * Verarbeitung einer neuen Nachricht.
	 * 
	 * @param m
	 *            Message
	 */
	public void processNewMessage(Message m) {
		boolean valid = validateMessage(m);
		if (!valid) {
			throw new IllegalArgumentException(
					"Uebergebene Message ist nicht valide! Message: " + m);
		}

		List<User> users = ServiceLocator.getInstance().getRegistryService()
				.getUsers();

		String name = m.getName();
		JTextArea textArea = nameTextAreaMap.get(name);

		if (textArea != null) {
			String oldText = textArea.getText();
			textArea.setText(getFormattedMessage(m.getName(), m.getText(),
					oldText));
		} else {
			String text = getFormattedMessage(m.getName(), m.getText());
			addToTabPane(m.getName(), text);
			// focusTabPane(m.getName());
		}

		System.out.println("Message: Name: " + m.getName() + " Text: "
				+ m.getText());

	}

	/**
	 * Gibt die Message formatiert aus.
	 * 
	 * @param username
	 *            Username
	 * @param msg
	 *            Nachricht
	 * @param oldText
	 *            vorhandener text in der text area
	 * @return formatierter String
	 */
	public String getFormattedMessage(String username, String msg,
			String oldText) {
		return oldText + "\n" + getFormattedMessage(username, msg);

	}

	/**
	 * Gibt die Message formatiert aus.
	 * 
	 * @param username
	 *            Username
	 * @param msg
	 *            Nachricht
	 * @param oldText
	 *            vorhandener text in der text area
	 * @return formatierter String
	 */
	public String getFormattedMessage(String username, String msg) {
		return "[" + df.format(new Date()) + "] " + username + "> " + msg;
	}

	/**
	 * Validiert die neue nachricht
	 * 
	 * @param m
	 */
	public boolean validateMessage(Message m) {

		return true;
	}

	/**
	 * Fuegt ein SendReceiveMessage Tab fuer diese Konversation ein.
	 * 
	 * @param label
	 *            Label des Tabs
	 * @param text
	 *            Text der in die TextArea geschrieben wird.
	 */
	public void addToTabPane(String label, String text) {
		JPanel textPanel = new JPanel(new BorderLayout());

		final JTextArea timelineTextArea = new JTextArea(5, 70);
		if (text != null) {
			timelineTextArea.setText(text);
		}
		timelineTextArea.setWrapStyleWord(true);
		timelineTextArea.setLineWrap(true);
		timelineTextArea.setEditable(false);
		timelineTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		JScrollPane textScrollPane = new JScrollPane(timelineTextArea);

		textScrollPane.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						timelineTextArea.select(
								timelineTextArea.getHeight() + 1000, 0);
					}
				});

		textPanel.add(textScrollPane, BorderLayout.CENTER);

		JTextArea messageTextArea = new JTextArea(2, 70);
		messageTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setLineWrap(true);
		JScrollPane messageScrollPane = new JScrollPane(messageTextArea);

		textPanel.add(messageScrollPane, BorderLayout.SOUTH);

		tabbedPane.addTab(label, textPanel);

		nameTextAreaMap.put(label, timelineTextArea);
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
