package devhood.im.sim.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.dao.interfaces.UserDao;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.UserChangeListener;
import devhood.im.sim.service.interfaces.UserService;

/**
 * Dieser {@link UserService} aktualisiert sich selbst und den übergebenen User
 * regelmäßig.
 * 
 * @author flo
 * 
 */
@Named("refreshingUserService")
public class RefreshingUserService implements UserService, EventObserver {
	/**
	 * Aktuelle User im System.
	 */
	private List<User> currentUsers = new ArrayList<User>();

	/**
	 * UserDao zum Zugriff auf Stammdaten, zb User.
	 */
	@Inject
	private UserDao userDao;

	/**
	 * true wenn user min. einmal refresht wurden.
	 */
	private boolean usersLoaded;

	/**
	 * Listener wird aufgerufen, wenn User im System hinzu- oder wegkommen.
	 */
	private List<UserChangeListener> userChangeListeners = new ArrayList<UserChangeListener>();

	/**
	 * Konfiguration.
	 */
	@Inject
	private SimConfiguration simConfiguration;

	/**
	 * Initialisiert, füllt das UserPanel.
	 */
	public void init() {
		refreshUsers();
		startUserRefreshingTimer();
	}

	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.SERVER_INITIALISED.equals(event)) {
			refresh(simConfiguration.getCurrentUser());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isUserOnline(String user) {
		boolean online = false;
		for (User u : currentUsers) {
			if (u.getName().equals(user)) {
				online = true;
			}
		}

		return online;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<User> getUsers() {
		return userDao.getUsers();
	}

	/**
	 * Fuegt die Users aus registryService in die Liste ein.
	 */
	public void refreshUsers() {
		List<User> users = userDao.getUsers();
		processNewOrRemovedUsers(users);
	}

	/**
	 * Aktualisiert den Nutzer, startet einen Timer, der das regelmaessig macht.
	 */
	@Override
	public void refresh(User u) {
		Timer t = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				try {
					User u = simConfiguration.getCurrentUser();
					u.setLastaccess(new Date());
					userDao.refresh(u);
				} catch (Exception e) {
					// Wenn Exception fliegt, soll der Timer weiterlaufen.
					e.printStackTrace();
				}
			};
		};

		t.schedule(task, 0, simConfiguration.getInitRefreshUserStateInterval());
	}

	/**
	 * Verarbeitet die USer und prüft ob sie bereits vorhanden, oder neu, oder
	 * nicht mehr vorhanden sind. Benachrichtigt den userChangeListener in
	 * beiden fällen.
	 * 
	 * @param users
	 *            aktuelle USer aus der dB.
	 */
	public void processNewOrRemovedUsers(List<User> users) {
		List<User> newUsers = new ArrayList<User>();
		List<User> offlineUsers = new ArrayList<User>();

		for (User u : users) {
			if (currentUsers.size() == 0) {
				currentUsers.add(u);
				continue;
			}

			if (!currentUsers.contains(u)) {
				if (!simConfiguration.getUsername().equals(u.getName())) {
					newUsers.add(u);
				}
				currentUsers.add(u);
			}
		}

		Iterator<User> it = currentUsers.iterator();
		while (it.hasNext()) {
			User user = it.next();
			if (!users.contains(user)) {
				if (!simConfiguration.getUsername().equals(user.getName())) {
					offlineUsers.add(user);
				}
				it.remove();
			}
		}

		if (offlineUsers.size() > 0) {
			if (userChangeListeners.size() > 0) {
				for (UserChangeListener userChangeListener : userChangeListeners) {
					userChangeListener.userRemoved(offlineUsers);
				}
			}
		}

		if (userChangeListeners.size() > 0) {
			if (newUsers.size() > 0) {
				for (UserChangeListener userChangeListener : userChangeListeners) {
					userChangeListener.userAdded(newUsers);
				}
			}
		}
	}

	/**
	 * Fuegt einen {@link UserChangeListener} ein.
	 * 
	 * @param listener
	 *            Listener.
	 */
	@Override
	public void addUserChangeListener(UserChangeListener listener) {
		this.userChangeListeners.add(listener);
	}

	/**
	 * Startet den Thread, der die User liste aktuell haelt.
	 */
	public void startUserRefreshingTimer() {
		Timer reloadUserTimer = new Timer(
				"RefreshingUserService: Reload Users Timer");

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					userDao.purgeOfflineUsers();

					refreshUsers();
					usersLoaded = true;
				} catch (Exception e) {
					// Sollte eine Exception fliegen, soll der Timer
					// weiterlaufen.
					e.printStackTrace();
				}
			}
		};
		reloadUserTimer.schedule(task, simConfiguration.getUserLoadDelay(),
				simConfiguration.getUserLoadPeriod());
	}

	/**
	 * Gibt die aktuellen User zurueck. Falls bisher noch keine USer geladen
	 * wurden, wird eine Liste zurueckgegeben, die bei contains immer true
	 * zurueck gibt. (Da noch nicht klar ist, ob der User vorhanden ist oder
	 * nicht, hacky).
	 * 
	 * @return list von usern.
	 */
	public List<User> getCurrentUsers() {
		List<User> users = new ArrayList<User>();
		if (!usersLoaded) {
			users = new ArrayList<User>() {
				public boolean contains(Object o) {
					return true;
				};
			};
		} else {
			users = currentUsers;
		}
		return users;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void purgeOfflineUsers() {
		userDao.purgeOfflineUsers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logout(String username) {
		userDao.logout(username);
	}

	public User getUser(String name) {
		return userDao.getUser(name);
	}

}