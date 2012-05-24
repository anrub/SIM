package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComponentInputMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.controller.SimControl;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.FileSendRequestMessage;
import devhood.im.sim.model.Message;
import devhood.im.sim.model.MessageType;
import devhood.im.sim.model.MessagingError;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.MessageSender;
import devhood.im.sim.service.interfaces.UserService;
import devhood.im.sim.ui.util.SimpleTabCache;
import devhood.im.sim.ui.util.SmileyFactory;
import devhood.im.sim.ui.util.Splitter;
import devhood.im.sim.ui.util.UiUtil;
import devhood.im.sim.ui.util.UserColorFactory;

/**
 * Panel to send and receive messages.
 * 
 * @author flo
 * 
 */
@Named("sendReceiveMessagePanel")
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

	@Inject
	private SimControl simControl;

	@Inject
	private UserService userService;

	@Inject
	private UserPanel userPanel;

	@Inject
	private UserColorFactory userColorFactory;

	@Inject
	private SmileyFactory smileyFactory;

	@Inject
	private SimConfiguration simConfiguration;

	@Inject
	private ApplicationContext applicationContext;

	/**
	 * Simpler cache für die tabs.
	 */
	@Inject
	private SimpleTabCache nameTimelineCache;

	public void init() {
		setLayout(new BorderLayout());

		tabbedPane = new JTabbedPane();

		tabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = tabbedPane.getSelectedIndex();
					if (index == 0) {
						return;
					}
					closeTab(index);
				}
			}
		});

		addToTabPane(simConfiguration.getStreamTabName(), null);

		// Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel(new BorderLayout());

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (tabbedPane.getSelectedIndex() != 0) {
					String toUser = getCurrentSelectedTabTitle();
					sendMessage(toUser);
				} else {
					sendMessage(simConfiguration.getStreamTabName());
				}
			}
		});

		buttonPane.add(sendButton, BorderLayout.CENTER);

		JPanel buttonsRight = new JPanel();
		JButton clearButton = new JButton("Clear");

		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clearText(nameTextAreaMap.get(getCurrentSelectedTabTitle()));
			}
		});

		// Schlieï¿½t das aktuelle Tab.
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = tabbedPane.getSelectedIndex();
				if (index == 0) {
					return;
				}
				closeTab(index);
			}
		});

		final JFrame smileyFrame = new JFrame();
		smileyFrame.setIconImage(UiUtil
				.createImage("/images/yahoo_smileys/01.gif"));
		GridBagLayout layout = new GridBagLayout();

		final JPanel panel = new JPanel(layout);

		SwingWorker<Void, Void> w = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				smileyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				final GridBagConstraints c = new GridBagConstraints();

				int x = 0;
				int y = 0;
				Set<String[]> keySet = smileyFactory.getSmileys().keySet();

				Map<String, String[]> tempMapForSorting = new HashMap<String, String[]>();
				List<String> keyListForSorting = new ArrayList<String>();
				for (String[] keys : keySet) {
					tempMapForSorting.put(keys[0], keys);
					keyListForSorting.add(keys[0]);
				}

				Collections.sort(keyListForSorting);

				for (String key : keyListForSorting) {
					c.gridx = x;
					c.gridy = y;
					String smileyCode = key.replace("&gt;", ">")
							.replace("&lt;", "<").replace("&amp;", "&")
							.replace("&quot;", "\"");
					ImageIcon img = UiUtil.createImageIcon(
							"/images/yahoo_smileys/"
									+ smileyFactory.getSmileys().get(
											tempMapForSorting.get(key)),
							smileyCode);
					JLabel smileyLabel = new JLabel(img);
					smileyLabel.setToolTipText(smileyCode);

					smileyLabel.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseClicked(MouseEvent e) {
							JLabel l = (JLabel) e.getSource();
							ImageIcon icon = (ImageIcon) l.getIcon();
							String code = icon.getDescription();
							JTextArea input = inputTextAreaMap
									.get(getCurrentSelectedTabTitle());
							input.insert(" " + code + " ",
									input.getCaretPosition());

							focusMessageTextArea(getCurrentSelectedTabTitle());

							smileyFrame.dispose();
						}

					});

					if (y < 10) {
						y++;
					} else {
						y = 0;

						x++;
					}

					panel.add(smileyLabel, c);
				}
				return null;
			}
		};
		w.execute();

		smileyFrame.add(panel);

		final JButton smileyButton = new JButton(UiUtil.createImageIcon(
				"/images/yahoo_smileys/01.gif", ":)"));
		smileyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.repaint();
				smileyFrame.pack();
				smileyFrame.setVisible(true);
				smileyFrame.setLocationRelativeTo(smileyButton);
			}
		});
		buttonsRight.add(smileyButton);
		// buttonsRight.add(statusComboBox);
		buttonsRight.add(clearButton);
		buttonsRight.add(closeButton);

		buttonPane.add(buttonsRight, BorderLayout.EAST);

		this.add(tabbedPane, BorderLayout.CENTER);
		this.add(buttonPane, BorderLayout.PAGE_END);

		createUnreadMessagesTimer();

		EventDispatcher.add(this);

	}

	/**
	 * Loescht den Text aus der Textarea.
	 * 
	 * @param area
	 *            textarea.
	 */
	public void clearText(JTextComponent area) {

		if (area instanceof JEditorPane) {

			String tableLayout = "<table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">";
			tableLayout += "<tr><td colspan=\"2\" valign=\"top\"><i>Bildschirminhalt gelöscht</i></td></tr>";
			tableLayout += "<tr><td colspan=\"2\" valign=\"top\"><br/></td></tr></table>";

			area.setText("<html><head></head><body></body></html>");

			String oldText = area.getText();
			StringBuffer clearTableLayout = new StringBuffer(oldText);

			int index = oldText.indexOf("</body>");

			clearTableLayout.insert(index, tableLayout);
			area.setText(clearTableLayout.toString());
		} else {
			area.setText(null);
		}

		area.moveCaretPosition(0);
	}

	/**
	 * Erzeugt den Timer, der regelmäßig checkt ob es ungelesene Nachrichten
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
		String title = tabbedPane.getTitleAt(index);
		return title;
	}

	/**
	 * Schlieï¿½t den Tab mit dem angegebenen index:
	 * 
	 * @param index
	 *            tab
	 */
	protected void closeTab(int index) {
		if (index != 0) {
			String title = tabbedPane.getTitleAt(index);
			// Vor dem schliessen wird der Inhalt in den Cache geschrieben.
			try {
				nameTimelineCache.put(title, nameTextAreaMap.get(title)
						.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
			nameTextAreaMap.remove(title);
			inputTextAreaMap.remove(title);

			tabbedPane.remove(index);

			addKeyboardShortcuts(tabbedPane);
		}
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

		if (StringUtils.isEmpty(input.getText().trim())) {
			// clearText(input);
			return;
		}

		final Message newMessage = new Message();

		List<String> usernames = new ArrayList<String>();
		usernames.add(toUser);
		newMessage.setReceiver(usernames);
		newMessage.setSender(simConfiguration.getUsername());
		newMessage.setText(input.getText());

		clearText(input);

		if (toUser.equals(simConfiguration.getStreamTabName())) { // Ist dies im
																	// Stremtab
																	// -
																	// nachricht
			// an alle
			newMessage.setMessageType(MessageType.ALL);
			newMessage.getReceiver().clear();
			List<User> users = userService.getUsers();
			for (User user : users) {
				newMessage.getReceiver().add(user.getName());
			}
			System.out.println("Nachricht an alle");
		}

		if (!toUser.equals(simConfiguration.getStreamTabName())) { // Ist dies
																	// im
																	// Stremtab
																	// -
																	// nachricht
			// an alle

			if (userService.isUserOnline(toUser)) {
				timeline.setText(getFormattedMessage(newMessage,
						timeline.getText()));
			} else {
				outputStatusMessage("Tut mir leid, " + toUser + " ist offline",
						timeline);
			}

		}

		// clearText(input);

		moveCaretDown((JEditorPane) timeline);

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
				simControl.sendMessage(newMessage);
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
			String text = o.toString();

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
			// Meldung dass Benutzer offline oder online ist
		} else if (Events.USER_OFFLINE_NOTICE.equals(event)
				|| Events.USER_ONLINE_NOTICE.equals(event)) {
			String message = "User "
					+ o
					+ " ist jetzt "
					+ (Events.USER_OFFLINE_NOTICE.equals(event) ? "offline"
							: "online");
			outputStatusMessage(message,
					nameTextAreaMap.get(simConfiguration.getStreamTabName()));

			outputStatusMessage(message, (List<User>) o);

		} else if (Events.MESSAGE_SEND_FAILED.equals(event)) {

			final MessagingError error = (MessagingError) o;
			if (MessageType.SINGLE.equals(error.getMessage().getMessageType())) {
				// Muss per invokeLater auf dem Swing Event Dispatcher Thread
				// ausgefï¿½hrt werden siehe
				// http://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html
				// http://tips4java.wordpress.com/2008/10/22/text-area-scrolling/
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						JTextComponent textarea = nameTextAreaMap.get(error
								.getMessage().getReceiver().get(0));
						if (textarea == null) {
							textarea = nameTextAreaMap.get(simConfiguration
									.getStreamTabName());
						}
						outputStatusMessage(
								"Fehler: Message konnte nicht gesendet werden: "
										+ error.getException().getMessage(),
								textarea);
					}
				});
			}
		} else if (Events.MESSAGE_FILE_REQUEST_RECEIVED.equals(event)) {
			ReceiveFileFrame frame = new ReceiveFileFrame();
			frame.init();
			frame.setMessageSender((MessageSender) applicationContext
					.getBean(MessageSender.class));
			frame.setFileSendRequestMessage((FileSendRequestMessage) o);
			frame.showFrame();
		}
	}

	/**
	 * Z Verarbeitung einer neuen Nachricht.
	 * 
	 * @param m
	 *            Message
	 */
	protected synchronized void processNewMessage(Message m) {
		String sender = m.getSender();

		if (MessageType.ALL.equals(m.getMessageType())) {
			sender = simConfiguration.getStreamTabName();
		}
		if (MessageType.USER_STATUS.equals(m.getMessageType())) {
			// Wenn eine Statusnachricht von sich selbst kommt, nicht
			// verarbeiten
			if (simConfiguration.getUsername().equals(m.getSender())) {
				return;
			}

			String statusMessage = getFormattedUserStatusMessage(m);
			outputStatusMessage(statusMessage,
					simConfiguration.getStreamTabName(), sender);
		}
		if (!MessageType.USER_STATUS.equals(m.getMessageType())) {
			final JEditorPane textArea = nameTextAreaMap.get(sender);

			if (textArea != null) {
				String oldText = textArea.getText();
				synchronized (this) {
					textArea.setText(getFormattedMessage(m, oldText));
					moveCaretDown(textArea);
				}
			} else {
				String textline = getFormattedMessage(m);
				addToTabPane(sender, textline);
			}

			// Icon auf ungelesen setzten, falls tab derzeit nicht ausgewaehlt.
			if (!isTabSelected(sender)) {
				setIconToUnreadMessages(sender);
			}
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
	 * Gibt eine Statusmessage aus
	 * 
	 * @param statusMessage
	 *            beliebige Statusmeldung
	 * @param textArea
	 *            Zieltab fï¿½r Statusmeldung
	 */
	protected void outputStatusMessage(String statusMessage,
			JTextComponent textArea) {
		String oldText = textArea.getText();
		synchronized (this) {
			textArea.setText(getFormattedMessage(
					"<tr><td colspan=\"2\" valign=\"top\"><i>" + statusMessage
							+ "</td></tr></i>", oldText));
		}
	}

	/**
	 * GIbt die Nachricht in den Tabs aus.
	 * 
	 * @param statusMessage
	 *            Nachricht
	 * @param tabs
	 *            Liste von Tabs
	 */
	protected void outputStatusMessage(String statusMessage, List<User> tabs) {
		for (User user : tabs) {
			outputStatusMessage(statusMessage, user.getName());
		}
	}

	protected void outputStatusMessage(String statusMessage, String... tabs) {
		for (String tab : tabs) {
			JEditorPane textArea = nameTextAreaMap.get(tab);
			if (textArea != null) {
				outputStatusMessage(statusMessage, textArea);
			}
		}
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
		String message = "";
		if (m != null) {
			message += getFormattedMessage(m);
		}
		return getFormattedMessage(message, oldText);
	}

	/**
	 * Gibt einen beliebigen Text formatiert aus.
	 * 
	 * @param message
	 *            Nachricht als Text
	 * @param oldText
	 *            vorhandener Text in der Textarea
	 * @return formatierter String
	 */
	protected String getFormattedMessage(String message, String oldText) {
		StringBuffer newMsg = new StringBuffer(oldText);

		if (oldText.contains("</table>\n  </body>")) {
			int index = oldText.indexOf("</table>\n  </body>");
			newMsg.insert(index, message);
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
		String text = StringEscapeUtils.escapeHtml4(m.getText());
		String sender = m.getSender();
		String colorHexValue = "#000000";
		String[] chunks = text.split("\\s");
		String linkPattern = "((file\\:|mailto\\:|(news|(ht|f)tp(s?))\\://){1}\\S+)";
		StringBuffer msg = new StringBuffer();

		/* Farbe des Benutzers ermitteln. */
		if (simConfiguration.getUsername().equals(sender)) {
			colorHexValue = simConfiguration.getUserColor();
		} else {
			colorHexValue = userColorFactory.getOrReserveUserColor(sender)
					.getHexValue();
		}

		for (String c : chunks) {
			if (c.matches(linkPattern)) {
				c = "<a href=\"" + c + "\" alt=\"" + c + "\">"
						+ shorten(c, simConfiguration.getMaxLinkLength())
						+ "</a>";
			} else {
				c = smileyFactory.applySmiles(c);
			}
			msg.append(" ");
			msg.append(c);
		}

		return "<tr><td valign=\"top\" nowrap><font color=\"" + colorHexValue
				+ "\">[" + df.format(new Date()) + "] " + m.getSender()
				+ " ></font>&nbsp;</td><td valign=\"top\" width=\"100%\">"
				+ msg.toString() + "</td></tr>";
	}

	/**
	 * Bricht den String nach chars anzahl von Zeichen mit &lt;br&gt; um.
	 * 
	 * @param s
	 *            String
	 * @return verkuerzter String
	 */
	public String shorten(String s, int chars) {
		String shortened = "";
		List<String> parts = Splitter.getParts(s, chars);
		for (int i = 0; i < parts.size(); i++) {
			shortened = shortened + parts.get(i);
			if (i < parts.size()) {
				shortened = shortened + "<br/>";
			}
		}

		return shortened;
	}

	/**
	 * Gibt die USerstatus message zurueck.
	 * 
	 * @param m
	 *            Message
	 * @return string msg.
	 */
	protected String getFormattedUserStatusMessage(Message m) {
		String msg = "User [" + m.getSender() + "] ist jetzt ";
		msg = msg + " " + m.getUserStatus().getText();

		return msg;
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
		String cachedText = nameTimelineCache.get(label);
		String layoutedText = "";
		if (cachedText == null) {
			layoutedText = "<table width=\"100%\" height=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">";

			if (!simConfiguration.getStreamTabName().equalsIgnoreCase(label)) {
				layoutedText += "<tr><td colspan=\"2\" valign=\"top\"><i>Konversation mit User ["
						+ label + "] gestartet</i></td></tr>";
			} else {
				layoutedText += "<tr><td colspan=\"2\" valign=\"top\"><i>"
						+ simConfiguration.getApplicationName()
						+ "<br />Achtung: alles im Stream Tab wird an alle Teilnehmer geschickt!</i></td></tr>";
			}

			// layoutedText +=
			// "<tr><td colspan=\"2\" valign=\"top\"><br/></td></tr>";

			if (text != null) {
				layoutedText += text;
			}

			layoutedText += "</table>";
		} else {
			if (text == null) {
				text = "";
			}
			layoutedText = getFormattedMessage(text, cachedText);
		}

		JPanel textPanel = new JPanel(new BorderLayout());

		JScrollPane timelineScrollPane = createTimelineScrollpane(label,
				layoutedText);

		textPanel.add(timelineScrollPane, BorderLayout.CENTER);

		final JTextArea messageTextArea = new JTextArea(3, 70);
		messageTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		messageTextArea.setWrapStyleWord(true);
		messageTextArea.setLineWrap(true);

		// Schickt die Nachricht auf Enter.
		messageTextArea.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage(label);
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					clearText(messageTextArea);
				}
			}
		});

		JScrollPane messageScrollPane = new JScrollPane(messageTextArea);

		textPanel.add(messageScrollPane, BorderLayout.SOUTH);

		tabbedPane.addTab(label, simConfiguration.getReadIcon(), textPanel);

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

				scrollToBottom(title);
			}
		});

		addKeyboardShortcuts(tabbedPane);

		inputTextAreaMap.put(label, messageTextArea);
		scrollToBottom(label);

	}

	public void scrollToBottom(String label) {
		JEditorPane pane = nameTextAreaMap.get(label);
		Rectangle rect = new Rectangle(0, pane.getHeight(), (int) pane
				.getVisibleRect().getWidth(), (int) pane.getVisibleRect()
				.getHeight());
		pane.scrollRectToVisible(rect);
	}

	/**
	 * wechsetl das tab.
	 * 
	 * @param e
	 *            e
	 */
	public void switchTab(ActionEvent e) {
		String cmd = e.getActionCommand();
		tabbedPane.setSelectedIndex(new Integer(cmd) - 1);
	}

	/**
	 * Gibt das ausgewaehlte tab zurueck.
	 * 
	 * @return index.
	 */
	public int getCurrentSelectTabIndex() {
		return tabbedPane.getSelectedIndex();
	}

	/**
	 * Fuegt die Shortcuts ALT+1-9 zum tabpane hinzu. Damit kann man per alt+1-9
	 * zwischen den tabs umschalten.
	 * 
	 * @param tabbedPane
	 *            tabbedpane
	 */
	public void addKeyboardShortcuts(JTabbedPane tabbedPane) {
		if (getRootPane() != null) {
			InputMap inputmap = new ComponentInputMap(getRootPane());

			for (int i = 0; i < tabbedPane.getTabCount(); i++) {
				inputmap.put(KeyStroke.getKeyStroke("control " + (i + 1)),
						"switchTab");
			}

			inputmap.put(KeyStroke.getKeyStroke("control F4"),
					"closeCurrentTab");

			tabbedPane.setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inputmap);

			Action switchTab = new AbstractAction("switchTab") {

				@Override
				public void actionPerformed(ActionEvent e) {
					switchTab(e);
				}
			};

			Action closeCurrentTab = new AbstractAction("closeCurrentTab") {

				@Override
				public void actionPerformed(ActionEvent e) {
					closeTab(getCurrentSelectTabIndex());
				}
			};

			tabbedPane.getActionMap().put("switchTab", switchTab);
			tabbedPane.getActionMap().put("closeCurrentTab", closeCurrentTab);
		}
	}

	/**
	 * Schiebt den Cursor ans Ende des Documents.
	 * 
	 * @param textArea
	 *            textArea.
	 */
	public void moveCaretDown(final JEditorPane textArea) {
		textArea.setCaretPosition(textArea.getDocument().getLength());
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

		final JEditorPane timelineTextArea = new JEditorPane("text/html",
				"<html><head></head><body></body></html>");

		String oldText = timelineTextArea.getText();
		StringBuffer newMsg = new StringBuffer(oldText);

		if (oldText.contains("</body>")) {
			int index = oldText.indexOf("</body>");
			newMsg.insert(index, text);
		}

		timelineTextArea.setText(newMsg.toString());

		DefaultCaret caret = (DefaultCaret) timelineTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		timelineTextArea.setEditable(false);
		timelineTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

		timelineTextArea.addHyperlinkListener(new HyperlinkListener() {

			/**
			 * Bei Klick auf einen Link in der Timeline, wird der Browser
			 * geï¿½ffnet.
			 * 
			 * @param e
			 *            Event.
			 */
			@Override
			public void hyperlinkUpdate(final HyperlinkEvent e) {

				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					UiUtil.openUrlInBrowser(e.getURL().toString());
				}
			}
		});
		nameTextAreaMap.put(label, timelineTextArea);

		JScrollPane timelineScrollPane = new JScrollPane(timelineTextArea);

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
			JTextComponent inputComponent = inputTextAreaMap.get(name);
			if (inputComponent != null) {
				inputComponent.requestFocusInWindow();
			}
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
		tabbedPane.setIconAt(index, simConfiguration.getUnreadIcon());

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
		tabbedPane.setIconAt(index, simConfiguration.getReadIcon());
	}
}
