package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;

import devhood.im.sim.Sim;
import devhood.im.sim.model.Message;
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

	/**
	 * Map von Name Des Tabs -> Texarea des Msg.
	 */
	private Map<String, JEditorPane> nameTextAreaMap = new HashMap<String, JEditorPane>();

	/**
	 * Map von Name Des Tabs -> Texarea des Msg.
	 */
	private Map<String, JTextArea> inputTextAreaMap = new HashMap<String, JTextArea>();

	/**
	 * DateFormat.s
	 */
	private DateFormat df = new SimpleDateFormat("HH:mm:ss");

	/**
	 * Icon der gelesenen Nachrichten.
	 */
	private ImageIcon readIcon = createImageIcon("/images/read.png",
			"Nachrichten gelesen");

	/**
	 * Icon der ungelesenen Nachrichten.
	 */
	private ImageIcon unreadIcon = createImageIcon("/images/unread.png",
			"Ungelesene Nachrichten");

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
				sendMessage();
			}
		});

		buttonPane.add(sendButton);
		buttonPane.add(new JButton("Cancel"));

		// Schlieﬂt das aktuelle Tab.
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = tabbedPane.getSelectedIndex();
				Component c = tabbedPane.getComponentAt(index);

				String title = tabbedPane.getTitleAt(index);
				nameTextAreaMap.remove(title);
				inputTextAreaMap.remove(title);

				tabbedPane.remove(index);
			}
		});
		buttonPane.add(closeButton);

		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.PAGE_END);

		EventDispatcher.add(this);
	}

	/**
	 * Versendet die Nachricht, aktualisiert die UI.
	 */
	protected void sendMessage() {
		JPanel p = (JPanel) tabbedPane.getSelectedComponent();

		JTextComponent timeline = (JTextComponent) ((JViewport) ((JScrollPane) p
				.getComponent(0)).getComponent(0)).getComponent(0);
		JTextComponent input = (JTextComponent) ((JViewport) ((JScrollPane) p
				.getComponent(1)).getComponent(0)).getComponent(0);

		timeline.setText(getFormattedMessage(Sim.getUsername(),
				input.getText(), timeline.getText()));

		input.setText(null);

		input.requestFocusInWindow();
	}

	/**
	 * Empfaengt Events.
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		// Ein user wurde per UserPanel ausgeawehlt, nun wird das Tab
		// fokussiert.
		if (Events.USER_SELECTED.equals(event)) {
			JCheckBox box = (JCheckBox) o;
			String text = box.getText();

			int index = tabbedPane.indexOfTab(text);
			if (index == -1) {
				addToTabPane(text, null);
			}
			focusTabPane(text);
			// Neue Nachricht verarbeiten.
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
			// Zeigt die Tabbed pane mit dem entsprechenden Namen.
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
	protected void processNewMessage(Message m) {
		boolean valid = validateMessage(m);
		if (!valid) {
			throw new IllegalArgumentException(
					"Uebergebene Message ist nicht valide! Message: " + m);
		}

		String name = m.getName();
		String text = m.getText();

		JEditorPane textArea = nameTextAreaMap.get(name);

		if (textArea != null) {
			String oldText = textArea.getText();
			textArea.setText(getFormattedMessage(name, text, oldText));
		} else {
			String textline = getFormattedMessage(name, text);
			addToTabPane(m.getName(), textline);
			// focusTabPane(m.getName());
		}

		// Icon setzten, falls tab derzeit nicht ausgewaehlt.
		if (!isTabSelected(name)) {
			setIconToUnreadMessages(name);
		}

		System.out.println("Message: Name: " + name + " Text: " + text);

	}

	/**
	 * Gibt zurueck ob der Tab mit dem Namen derzeit ausgewaehlt ist oder nicht.
	 * 
	 * @param name
	 *            Name des Tabs
	 * @return true/false
	 */
	public boolean isTabSelected(String name) {
		int selected = tabbedPane.getSelectedIndex();
		int index = tabbedPane.indexOfTab(name);

		return selected == index;
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
	protected String getFormattedMessage(String username, String msg,
			String oldText) {
		StringBuffer newMsg = new StringBuffer(oldText);

		if (oldText.contains("</body>")) {
			int index = oldText.indexOf("</body>");

			newMsg.insert(index, "<br />" + getFormattedMessage(username, msg));
		}

		return newMsg.toString();

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
	protected String getFormattedMessage(String username, String msg) {
		return "[" + df.format(new Date()) + "] " + username + "> " + msg;
	}

	/**
	 * Validiert die neue nachricht
	 * 
	 * @param m
	 */
	private boolean validateMessage(Message m) {

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
	protected void addToTabPane(final String label, String text) {
		JPanel textPanel = new JPanel(new BorderLayout());

		final JEditorPane timelineTextArea = new JEditorPane("text/html", text);
		// if (text != null) {
		// timelineTextArea.setText(text);
		// }
		// timelineTextArea.setWrapStyleWord(true);
		// timelineTextArea.setLineWrap(true);

		timelineTextArea.setEditable(false);
		timelineTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		// timelineTextArea.get

		JScrollPane textScrollPane = new JScrollPane(timelineTextArea);

		textScrollPane.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						timelineTextArea.select(
								timelineTextArea.getHeight() + 1000, 0);
					}
				});

		textPanel.add(textScrollPane, BorderLayout.CENTER);

		JTextArea messageTextArea = new JTextArea(3, 70);
		messageTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setLineWrap(true);

		// Schickt die Nachricht auf Enter.
		messageTextArea.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

		JScrollPane messageScrollPane = new JScrollPane(messageTextArea);

		textPanel.add(messageScrollPane, BorderLayout.SOUTH);

		tabbedPane.addTab(label, textPanel);

		tabbedPane.addChangeListener(new ChangeListener() {

			/**
			 * Beim Tabwechsel wird der Fokus auf das Eingabefeld gelegt.
			 */
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane src = (JTabbedPane) e.getSource();
				int index = src.getSelectedIndex();
				String title = src.getTitleAt(index);

				focusMessageTextArea(title);

				unsetTabIcon(title);
			}
		});

		nameTextAreaMap.put(label, timelineTextArea);
		inputTextAreaMap.put(label, messageTextArea);
	}

	/**
	 * fokussiert das tabpane mit dem Namen.
	 * 
	 * @param name
	 *            Name des Panes.
	 */
	protected void focusTabPane(String name) {
		int index = tabbedPane.indexOfTab(name);
		if (index != -1) {
			tabbedPane.setSelectedIndex(index);
		}
	}

	/**
	 * Fokussiert das Feld im Tab mit dem Namen.
	 * 
	 * @param name
	 *            Name des Tabs.
	 */
	protected void focusMessageTextArea(String name) {
		inputTextAreaMap.get(name).requestFocusInWindow();
	}

	/**
	 * Setzt das Icon im Tab, so dass es auf ungelesene Nachrichten aufmerksam
	 * macht.
	 * 
	 * @param name
	 *            Name des Tabs.
	 */
	protected void setIconToUnreadMessages(String name) {
		ImageIcon icon = unreadIcon;
		int index = tabbedPane.indexOfTab(name);
		tabbedPane.setIconAt(index, icon);
	}

	/**
	 * Entfernt das Icon vom Tab und setzt das standardicon readicon.
	 * 
	 * @param name
	 *            Name des Tabs.
	 */
	protected void unsetTabIcon(String name) {
		int index = tabbedPane.indexOfTab(name);
		tabbedPane.setIconAt(index, readIcon);
	}

	/**
	 * Erzeugt ien ImageIcon aus dem uebergebenen path. path ist im classpath.
	 * 
	 * @param path
	 *            path aus classpath
	 * @param description
	 *            beschreibung
	 * @return imageicon
	 */
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
