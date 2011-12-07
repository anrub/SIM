package devhood.im.sim.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import devhood.im.sim.Sim;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.RegistryService;
import devhood.im.sim.ui.util.UiUtil;

/**
 * Panel zur Auswahl der User. Aktualisiert sich selbst via
 * {@link RegistryService}.
 * 
 * @author flo
 * 
 */
public class UserPanel extends JPanel {

	/**
	 * Aktuelle Usernamen im Panel.
	 */
	private List<String> currentUsers = new ArrayList<String>();

	/**
	 * RegistryService zum Zugriff auf Stammdaten, zb User.
	 */
	private RegistryService registryService;

	/**
	 * Laedt User 1 Sekunden nach dem Start.
	 */
	private int userLoadDelay = 1000;

	/**
	 * Laedt User alle 10 Sekunden.
	 */
	private int userLoadPeriod = 10000;

	/**
	 * true wenn user min. einmal refresht wurden.
	 */
	private boolean usersLoaded;

	public UserPanel(RegistryService registryService) {
		this.registryService = registryService;
		init();
	}

	/**
	 * Initialisiert, füllt das UserPanel.
	 */
	public void init() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);

		addUsers();
		startUserRefreshingTimer();
	}

	/**
	 * Verarbeitet die USer und prüft ob sie bereits vorhanden, oder neu, oder
	 * nicht mehr vorhanden sind.
	 * 
	 * @param users
	 *            aktuelle USer aus der dB.
	 */
	public void processNewOrRemovedUsers(List<User> users,
			List<String> usersFromDb) {
		if (!usersLoaded) {
			return;
		}

		List<String> newUsers = new ArrayList<String>();
		List<String> offlineUsers = new ArrayList<String>();

		for (User u : users) {
			if (currentUsers.size() == 0) {
				currentUsers.add(u.getName());
				continue;
			}

			if (!currentUsers.contains(u.getName())) {
				if (!Sim.username.equals(u.getName())) {
					newUsers.add(u.getName());
				}
				currentUsers.add(u.getName());
			}
		}

		Iterator<String> it = currentUsers.iterator();
		while (it.hasNext()) {
			String user = it.next();
			if (!usersFromDb.contains(user)) {
				if (!Sim.username.equals(user)) {
					offlineUsers.add(user);
				}
				it.remove();
			}
		}

		if (offlineUsers.size() > 0) {
			EventDispatcher.fireEvent(Events.USER_OFFLINE_NOTICE,
					offlineUsers.toString());
		}
		if (newUsers.size() > 0) {
			EventDispatcher.fireEvent(Events.USER_ONLINE_NOTICE,
					newUsers.toString());

		}
	}

	/**
	 * Fuegt die Users aus registryService in die Liste ein.
	 */
	public void addUsers() {
		List<User> users = registryService.getUsers();
		List<String> userFromDb = new ArrayList<String>();

		for (User user : users) {

			final JLabel userLabel = new JLabel(user.getName());
			userLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {

					EventDispatcher.fireEvent(Events.USER_SELECTED,
							userLabel.getText());

				}

			});
			userFromDb.add(user.getName());
			userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			userLabel.setIcon(UiUtil.createImageIcon("/images/16x16transparent.png", ""));
			
			userLabel.setMinimumSize(new Dimension(100, 20));
			add(userLabel);

		}

		processNewOrRemovedUsers(users, userFromDb);

	}

	/**
	 * Startet den Thread, der die User liste aktuell haelt.
	 */
	public void startUserRefreshingTimer() {
		Timer t = new Timer("Load Users Timer");
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				registryService.purgeOfflineUsers();

				removeAll();
				addUsers();
				validate();
				repaint();
				usersLoaded = true;
			}
		};

		t.schedule(task, userLoadDelay, userLoadPeriod);
	}

	/**
	 * Gibt die aktuellen User zurueck. Falls bisher noch keine USer geladen
	 * wurden, wird eine Liste zurueckgegeben, die bei contains immer true
	 * zurueck gibt. (Da noch nicht klar ist, ob der User vorhanden ist oder
	 * nicht, hacky).
	 * 
	 * @return list von usern.
	 */
	public List<String> getCurrentUsers() {
		List<String> users = new ArrayList<String>();
		if (!usersLoaded) {
			users = new ArrayList<String>() {
				public boolean contains(Object o) {
					return true;
				};
			};
		} else {
			users = currentUsers;
		}
		return users;
	}

	public void setCurrentUsers(List<String> currentUsers) {
		this.currentUsers = currentUsers;
	}

}
