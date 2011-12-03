package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
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
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;

import devhood.im.sim.Sim;
import devhood.im.sim.model.Message;
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

	/**
	 * Map von Name Des Tabs -> Texarea des Msg (Timeline).
	 */
	private Map<String, JEditorPane> nameTextAreaMap = new HashMap<String, JEditorPane>();

	/**
	 * Map von Name Des Tabs -> Texarea der Inputzeile (Text sendne).
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
				if (tabbedPane.getSelectedIndex() != 0) {
					String toUser = getCurrentSelectedTabTitle();
					sendMessage(toUser);
				}
			}
		});

		buttonPane.add(sendButton);
		buttonPane.add(new JButton("Cancel"));

		// Schließt das aktuelle Tab.
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = tabbedPane.getSelectedIndex();
				if (index == 0) {
					return;
				}
				String title = getCurrentSelectedTabTitle();
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
	 * Gibt den Titel des aktuellen Tabs zurueck.
	 * 
	 * @return Titel.
	 */
	protected String getCurrentSelectedTabTitle() {
		int index = tabbedPane.getSelectedIndex();

		Component c = tabbedPane.getComponentAt(index);

		String title = tabbedPane.getTitleAt(index);

		return title;
	}

	/**
	 * Versendet die Nachricht, aktualisiert die UI.
	 */
	protected void sendMessage(String toUser) {
		JPanel p = (JPanel) tabbedPane.getSelectedComponent();

		JTextComponent timeline = (JTextComponent) ((JViewport) ((JScrollPane) p
				.getComponent(0)).getComponent(0)).getComponent(0);
		JTextComponent input = (JTextComponent) ((JViewport) ((JScrollPane) p
				.getComponent(1)).getComponent(0)).getComponent(0);

		final Message newMessage = new Message();
		newMessage.setReceiver(toUser);
		newMessage.setSender(Sim.getUsername());
		newMessage.setText(input.getText());

		timeline.setText(getFormattedMessage(newMessage, timeline.getText()));

		input.setText(null);

		input.requestFocusInWindow();

		// Versand wird asynchron ausgefuehrt, da potentiell langsam und droht
		// die ui zu blocken.
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				ServiceLocator.getInstance().getMessageService()
						.sendMessage(newMessage);
			}
		});
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

		String sender = m.getSender();
		String text = m.getText();

		JEditorPane textArea = nameTextAreaMap.get(sender);

		if (textArea != null) {
			String oldText = textArea.getText();
			textArea.setText(getFormattedMessage(m, oldText));
		} else {
			String textline = getFormattedMessage(m);
			addToTabPane(sender, textline);
		}

		// Icon auf ungelesen setzten, falls tab derzeit nicht ausgewaehlt.
		if (!isTabSelected(sender)) {
			setIconToUnreadMessages(sender);
		}

		System.out.println("Message: " + m + ", received.");

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
	protected String getFormattedMessage(Message m, String oldText) {
		StringBuffer newMsg = new StringBuffer(oldText);

		if (oldText.contains("</body>")) {
			int index = oldText.indexOf("</body>");

			newMsg.insert(index, "<br />" + getFormattedMessage(m));
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
	protected String getFormattedMessage(Message m) {
		String text = m.getText();
		String[] chunks = text.split("\\s");
		String linkPattern = "((file\\:|mailto\\:|(news|(ht|f)tp(s?))\\://){1}\\S+)";
		StringBuffer msg = new StringBuffer();

		for (String c : chunks) {
			if (c.matches(linkPattern)) {
				c = "<a href=\"" + c + "\">" + c + "</a>";
			}
			msg.append(" ");
			msg.append(c);
		}

		return "[" + df.format(new Date()) + "] " + m.getSender() + "> "
				+ msg.toString();
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

		JScrollPane timelineScrollPane = createTimelineScrollpane(label, text);
		textPanel.add(timelineScrollPane, BorderLayout.CENTER);

		JTextArea messageTextArea = new JTextArea(3, 70);
		messageTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setLineWrap(true);

		// Schickt die Nachricht auf Enter.
		messageTextArea.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage(label);
				}
			}
		});

		JScrollPane messageScrollPane = new JScrollPane(messageTextArea);

		textPanel.add(messageScrollPane, BorderLayout.SOUTH);

		tabbedPane.addTab(label, readIcon, textPanel);

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

		inputTextAreaMap.put(label, messageTextArea);
	}

	/**
	 * Erzeugt das timeline Scrollpane incl inhalt.
	 * 
	 * @param label
	 *            Label des Tabs.
	 * @param text
	 *            Textinhalt.
	 * @return
	 */
	protected JScrollPane createTimelineScrollpane(String label, String text) {
		final JEditorPane timelineTextArea = new JEditorPane("text/html", text);

		timelineTextArea.setEditable(false);
		timelineTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		timelineTextArea.addHyperlinkListener(new HyperlinkListener() {

			/**
			 * Bei Klick auf einen Link in der Timeline, wird der Browser
			 * geöffnet.
			 * 
			 * @param e
			 *            Event.
			 */
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {

				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

					try {
						Desktop.getDesktop().browse(e.getURL().toURI());
					} catch (Exception uri) {
						System.out.println(uri);
					}

				}
			}
		});
		nameTextAreaMap.put(label, timelineTextArea);

		JScrollPane timelineScrollPane = new JScrollPane(timelineTextArea);

		timelineScrollPane.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent e) {
						timelineTextArea.select(
								timelineTextArea.getHeight() + 1000, 0);
					}
				});

		return timelineScrollPane;
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
