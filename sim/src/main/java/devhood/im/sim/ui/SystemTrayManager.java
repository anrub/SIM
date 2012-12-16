package devhood.im.sim.ui;

import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseListener;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.messages.model.BroadcastMessage;
import devhood.im.sim.messages.model.Message;
import devhood.im.sim.messages.model.RoomMessage;
import devhood.im.sim.messages.model.SingleMessage;
import devhood.im.sim.messages.model.UserStatusMessage;
import devhood.im.sim.service.interfaces.UserService;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.EventObserver;
import devhood.im.sim.ui.event.Events;

/**
 * Verwaltet das SystemTray.
 *
 * @author flo
 *
 */
@Named("systemTrayManager")
public class SystemTrayManager implements EventObserver {

	/**
	 * Von diesem User kam die letzte Nachricht.
	 */
	private String lastUser;

	@Inject
	private UserService userService;

	/**
	 * Referenz auf das TrayIcon.
	 */
	private TrayIcon systrayIcon;

	@Inject
	private SimConfiguration simConfiguration;

	@PostConstruct
	public void init() {
		EventDispatcher.add(this);

		final SystemTray tray = SystemTray.getSystemTray();

		Image trayIcon = simConfiguration.getTrayIcon();

		PopupMenu popup = new PopupMenu();
		systrayIcon = new TrayIcon(trayIcon,
				simConfiguration.getApplicationName(), popup);

		MenuItem item = new MenuItem("Exit");
		item.setLabel("Exit");
		item.addActionListener(new ExitAction(userService));

		popup.add(item);

		CheckboxMenuItem systrayMessages = new CheckboxMenuItem(
				"Systray Messages verbergen");
		systrayMessages.setState(false);

		systrayMessages.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					simConfiguration.setShowSystrayMessages(false);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					simConfiguration.setShowSystrayMessages(true);
				}

			}
		});

		popup.add(systrayMessages);

		try {
			tray.add(systrayIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Zeige bei Klick auf die Bubble-Message das Tab des letzten Users.
		systrayIcon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EventDispatcher.fireEvent(Events.SHOW_FRAME, null);
				EventDispatcher
						.fireEvent(Events.SHOW_MSG_TABBED_PANE, lastUser);
			}
		});
	}

	/**
	 * Wird vom {@link EventDispatcher} aufgerufen.
	 *
	 * @param Events
	 *            event siehe {@link Events}
	 * @param Object
	 *            o je nach Event verschiedene Parameter moeglich.
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.MESSAGE_RECEIVED.equals(event)) {
			Message m = (Message) o;
			if (m instanceof BroadcastMessage) {
				if (!m.getSender().contains(simConfiguration.getUsername())
						&& simConfiguration.isShowStreamInTray()) {
					displayMessage("Stream: " + m.getSender(), m.getText(),
							MessageType.INFO);
				}
				lastUser = simConfiguration.getStreamTabName();
			} else if (m instanceof SingleMessage) {
				displayMessage(m.getSender(), m.getText(), MessageType.INFO);
				lastUser = m.getSender();
			} else if (m instanceof RoomMessage) {
				displayMessage(
						"[" + ((RoomMessage) m).getRoomName() + "] "
								+ m.getSender(), m.getText(), MessageType.INFO);
				lastUser = m.getSender();
			} else if (m instanceof UserStatusMessage
					&& simConfiguration.isShowStatusInTray()) {
				if (!simConfiguration.getUsername().equals(m.getSender())) {
					displayMessage(m.getSender(), ((UserStatusMessage) m)
							.getUserStatus().getText(), MessageType.INFO);
					lastUser = m.getSender();
				}
			}

		} else if (Events.UNREAD_MESSAGES.equals(event)) {
			List<String> users = (List<String>) o;
			displayMessage("Ungelesene Nachrichten", users.toString(),
					MessageType.INFO);
		} else if (Events.USER_OFFLINE_NOTICE.equals(event)
				&& simConfiguration.isShowStatusInTray()) {
			displayMessage("User offline", o.toString(), MessageType.INFO);
		} else if (Events.USER_ONLINE_NOTICE.equals(event)
				&& simConfiguration.isShowStatusInTray()) {
			displayMessage("User online", o.toString(), MessageType.INFO);
		}

	}

	/**
	 * Zeigt die nachricht im systray an, wenn die Option nicht gewählt wurde.
	 *
	 * @param title
	 *            Titel
	 * @param message
	 *            Message
	 * @param messageType
	 *            MessageType
	 */
	public void displayMessage(String title, String message,
			MessageType messageType) {
		if (simConfiguration.isShowSystrayMessages()) {
			systrayIcon.displayMessage(title, message, messageType);
		}
	}

	/**
	 * Fuegt einen MouseListener an das SystmeTray.
	 *
	 * @param listener
	 *            Listener.
	 */
	public void addMouseListener(MouseListener listener) {
		systrayIcon.addMouseListener(listener);
	}

	class ExitAction implements ActionListener {
		private UserService userService;

		public ExitAction(UserService userService) {
			this.userService = userService;
		}

		/**
		 * Bei click auf Exit, Anwendung schließen.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			userService.logout(simConfiguration.getUsername());
			System.exit(0);

		}
	}
}