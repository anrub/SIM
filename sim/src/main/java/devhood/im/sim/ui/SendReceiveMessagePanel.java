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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;

import devhood.im.sim.Sim;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;

/**
 * Panel to send and receive messages.
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
	 * DateFormat.
	 */
	private DateFormat df = new SimpleDateFormat("HH:mm:ss");

	/**
	 * List mit ungelesenen Tabs.
	 */
	private List<String> unreadTabsList = new ArrayList<String>();

	public SendReceiveMessagePanel() {
		super();
		setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

		addToTabPane("Info", Sim.applicationName);

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

		// Schlie�t das aktuelle Tab.
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
				EventDispatcher.fireEvent(Events.UNSELECT_ALL_USERS, null);
			}
		});
		buttonPane.add(closeButton);

		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.PAGE_END);

		createUnreadMessagesTimer();

		EventDispatcher.add(this);
	}

	/**
	 * Erzeugt den Timer, der regelm��ig checkt ob es ungelesene Nachrichten
	 * gibt.
	 */
	public void createUnreadMessagesTimer() {
		Timer t = new Timer("Unread Messages Timer");
		TimerTask task = new TimerTask() {

			@Override
			public void run() {

				if (unreadTabsList.size() > 0) {
					EventDispatcher.fireEvent(Events.UNREAD_MESSAGES,
							unreadTabsList);
				}
			}
		};

		t.schedule(task, 0, 20000);
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

		// TODO fragil - haengt damit direkt mit der ui struktur zusammen
		JTextComponent timeline = (JTextComponent) ((JViewport) ((JScrollPane) p
				.getComponent(0)).getComponent(0)).getComponent(0);
		JTextComponent input = (JTextComponent) ((JViewport) ((JScrollPane) p
				.getComponent(1)).getComponent(0)).getComponent(0);

		final Message newMessage = new Message();
		newMessage.setReceiver(toUser);
		newMessage.setSender(Sim.getUsername());
		newMessage.setText(input.getText());

		String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		newMessage.setGroupChatmember(title);

		timeline.setText(getFormattedMessage(newMessage, timeline.getText()));

		input.setText(null);

		input.requestFocusInWindow();

		// Versand wird asynchron ausgefuehrt, da potentiell langsam und droht
		// die ui zu blocken.
		// TODO Fehlerhandling, wenn versand fehlschlaegt?
		// z.b. in einer liste Worker speichern und via Timer den return type
		// checken (wenn worker.isDone() == true, isdone ist auch true, wenn
		// gecancelt, falls einer false ist, eine Warnung in systray erzeugen.
		// evtl den betroffenen User rot markieren (letzter Versand nicht
		// erfolgreich)
		SwingWorker<Void, Message> worker = new SwingWorker<Void, Message>() {

			@Override
			protected Void doInBackground() throws Exception {
				EventDispatcher.fireEvent(Events.MESSAGE_SENT, newMessage);
				return null;
			}

			@Override
			protected void done() {
				// TODO Auto-generated method stub
				super.done();
			}
		};

		worker.execute();
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
		} else if (Events.GROUP_CHAT.equals(event)) {
			GroupChatModel m = (GroupChatModel) o;
			String tabtile = (m.getOldGroupChatName() != null
					&& m.getOldGroupChatName().length() > 0 ? m
					.getOldGroupChatName() : m.getGroupChatName());
			
			int index = tabbedPane.indexOfTab(tabtile);

			if (index == -1) {
				addToTabPane(tabtile, null);
			} else {
				JEditorPane pane = nameTextAreaMap.get(tabtile);
				nameTextAreaMap.remove(tabtile);
				nameTextAreaMap.put(m.getGroupChatName(), pane);

				JTextArea a = inputTextAreaMap.get(tabtile);
				inputTextAreaMap.remove(tabtile);
				inputTextAreaMap.put(m.getGroupChatName(), a);

				tabtile = m.getGroupChatName();
				tabbedPane.setTitleAt(index, tabtile);

			}

			focusTabPane(tabtile);
		}
	}

	/**
	 * Z Verarbeitung einer neuen Nachricht.
	 * 
	 * @param m
	 *            Message
	 */
	protected void processNewMessage(Message m) {
		String sender = m.getSender();
		String text = m.getText();
		String groupChatmembers = m.getGroupChatmember();

		if (groupChatmembers != null && groupChatmembers.length() > 0) {
			sender = groupChatmembers;
		}

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

		tabbedPane.addTab(label, Sim.readIcon, textPanel);

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

				setIconToReadMessages(title);
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
			 * ge�ffnet.
			 * 
			 * @param e
			 *            Event.
			 */
			@Override
			public void hyperlinkUpdate(final HyperlinkEvent e) {

				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

					// Fenster asynchron �ffnen, sonst blockt ui.
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

						@Override
						protected Void doInBackground() throws Exception {

							try {
								Desktop.getDesktop().browse(e.getURL().toURI());
							} catch (Exception uri) {
								// TODO Fehlerhandling, wen URI / Browser nicht
								// geoeffnet werden konnte. Warnung anzeigen.
								uri.printStackTrace();
							}

							return null;
						}

						@Override
						protected void done() {
							// TODO Auto-generated method stub
							super.done();
						}
					};

					worker.execute();

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
		if (!unreadTabsList.contains(name)) {
			unreadTabsList.add(name);
		}
		;
		int index = tabbedPane.indexOfTab(name);
		tabbedPane.setIconAt(index, Sim.unreadIcon);

	}

	/**
	 * Entfernt das Icon vom Tab und setzt das standardicon readicon.
	 * 
	 * @param name
	 *            Name des Tabs.
	 */
	protected void setIconToReadMessages(String name) {
		unreadTabsList.remove(name);
		int index = tabbedPane.indexOfTab(name);
		tabbedPane.setIconAt(index, Sim.readIcon);
	}
}
