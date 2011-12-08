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

import devhood.im.sim.Sim;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;
import devhood.im.sim.service.ServiceLocator;
import devhood.im.sim.service.interfaces.RegistryService;

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

	private RegistryService registryService = ServiceLocator.getInstance()
			.getRegistryService();

	/**
	 * Referenz auf das TrayIcon.
	 */
	private TrayIcon systrayIcon;

	/**
	 * Sollen systray messages gezeigt werden oder nicht.
	 */
	private boolean showSystrayMessages = true;

	
	/**
	 * Zeigt die Stream Meldungen im systray an oder nicht.
	 */
	private boolean showStreamInTray = true;
	
	/**
	 * Zeigt die Statusmeldungen im Tray an oder nicht.
	 */
	private boolean showStatusInTray = true;
	
	public SystemTrayManager() {
		init();
		EventDispatcher.add(this);
	}

	public void init() {
		final SystemTray tray = SystemTray.getSystemTray();

		Image trayIcon = Sim.trayIcon;

		PopupMenu popup = new PopupMenu();
		systrayIcon = new TrayIcon(trayIcon, Sim.applicationName, popup);

		MenuItem item = new MenuItem("Exit");
		item.setLabel("Exit");

		item.addActionListener(new ActionListener() {

			/**
			 * Bei click auf Exit, Anwendung schlie�en.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				registryService.logout(Sim.getCurrentUser().getName());
				System.exit(0);

			}
		});
		popup.add(item);
		
		
		CheckboxMenuItem systrayMessages = new CheckboxMenuItem("Systray Messages verbergen");
		systrayMessages.setState(false);
		
		systrayMessages.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() == ItemEvent.SELECTED) {
					showSystrayMessages = false;
				}else if ( e.getStateChange() == ItemEvent.DESELECTED) {
					showSystrayMessages = true;
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
			if (devhood.im.sim.model.MessageType.ALL.equals(m.getMessageType())) {
				if (!m.getSender().contains(Sim.getCurrentUser().getName()) && showStreamInTray) {
					displayMessage("Stream: " + m.getSender(), m.getText(),
							MessageType.INFO);
				}
				lastUser = Sim.streamTabName;
			} else if ( devhood.im.sim.model.MessageType.SINGLE.equals(m.getMessageType())){
				displayMessage(m.getSender(), m.getText(), MessageType.INFO);
				lastUser = m.getSender();
			}else if ( devhood.im.sim.model.MessageType.USER_STATUS.equals(m.getMessageType()) && showStatusInTray) {
				if ( !Sim.getCurrentUser().getName().equals(m.getSender())) {
					displayMessage(m.getSender(), m.getUserStatus().getText(), MessageType.INFO);
					lastUser = m.getSender();				
				}
			}

		} else if (Events.UNREAD_MESSAGES.equals(event)) {
			List<String> users = (List<String>) o;
			displayMessage("Ungelesene Nachrichten", users.toString(),
					MessageType.INFO);
		} else if (Events.USER_OFFLINE_NOTICE.equals(event)) {
			displayMessage("User offline", o.toString(), MessageType.INFO);
		} else if (Events.USER_ONLINE_NOTICE.equals(event)) {
			displayMessage("User online", o.toString(), MessageType.INFO);
		}

	}

	/**
	 * Zeigt die nachricht im systray an, wenn die Option nicht gew�hlt wurde.
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
		if (showSystrayMessages) {
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

	public boolean isShowSystrayMessages() {
		return showSystrayMessages;
	}

	public void setShowSystrayMessages(boolean showSystrayMessages) {
		this.showSystrayMessages = showSystrayMessages;
	}

	public boolean isShowStreamInTray() {
		return showStreamInTray;
	}

	public void setShowStreamInTray(boolean showStreamInTray) {
		this.showStreamInTray = showStreamInTray;
	}

	public boolean isShowStatusInTray() {
		return showStatusInTray;
	}

	public void setShowStatusInTray(boolean showStatusInTray) {
		this.showStatusInTray = showStatusInTray;
	}

}