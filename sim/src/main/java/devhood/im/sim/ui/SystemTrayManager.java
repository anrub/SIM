package devhood.im.sim.ui;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.List;

import devhood.im.sim.SimMain;
import devhood.im.sim.model.Message;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.EventObserver;
import devhood.im.sim.ui.event.Events;

/**
 * Verwaltet das SystemTray.
 * 
 * @author flo
 * 
 */
public class SystemTrayManager implements EventObserver {

	/**
	 * Von diesem User kam die letzte Nachricht.
	 */
	private String lastUser;

	/**
	 * Icon, dass im Tray angezeigt wird.
	 */
	private String trayIcon = "/images/trayIcon.gif";

	/**
	 * Referenz auf das TrayIcon.
	 */
	private TrayIcon systrayIcon;

	public void init() {
		final SystemTray tray = SystemTray.getSystemTray();

		String iconFilename = SimMain.class.getResource(trayIcon).getFile();
		Image image = Toolkit.getDefaultToolkit().getImage(iconFilename);

		PopupMenu popup = new PopupMenu();
		systrayIcon = new TrayIcon(image, "The Tip Text", popup);

		MenuItem item = new MenuItem("Exit");
		item.setLabel("Exit");

		item.addActionListener(new ActionListener() {

			/**
			 * Bei click auf Exit, Anwendung schlie�en.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}
		});

		popup.add(item);
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

		EventDispatcher.add(this);
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
			systrayIcon.displayMessage(m.getSender(), m.getText(),
					MessageType.INFO);
			lastUser = m.getSender();
		} else if (Events.UNREAD_MESSAGES.equals(event)) {
			List<String> users = (List<String>) o;
			systrayIcon.displayMessage("Ungelesene Nachrichten",
					users.toString(), MessageType.INFO);
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

}