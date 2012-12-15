package devhood.im.sim.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.dao.interfaces.RoomDao;
import devhood.im.sim.dao.interfaces.UserDao;
import devhood.im.sim.model.Room;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.UserChangeObserver;
import devhood.im.sim.service.interfaces.UserService;

/**
 * Dieser {@link UserService} aktualisiert sich selbst und den uebergebenen User
 * regelmaessg.
 *
 * @author flo
 *
 */
@Named("refreshingUserService")
public class RefreshingUserService implements UserService {

	/**
	 * Aktuelle User im System.
	 */
	private List<User> currentUsers = new ArrayList<User>();

	/**
	 * UserDao zum Zugriff auf Stammdaten, zb User.
	 */
	@Inject
	private UserDao userDao;

	@Inject
	private RoomDao roomDao;

	/**
	 * Listener wird aufgerufen, wenn User im System hinzu- oder wegkommen.
	 */
	private List<UserChangeObserver> userChangeListeners = new ArrayList<UserChangeObserver>();

	/**
	 * Liste von Usern, die momentan aktiv refresht werden. (eigtl nur der
	 * eigene aktuelle User).
	 */
	private List<String> refreshingUsers = new ArrayList<String>();

	/**
	 * Flag sagt, dass der Timer, der die aktuellen User aktualisiert, gestartet
	 * wurde.
	 */
	private boolean userRefreshingTimerStarted = false;

	/**
	 * Konfiguration.
	 */
	@Inject
	private SimConfiguration simConfiguration;

	/**
	 * Initialisiert.
	 */
	@PostConstruct
	public void init() {
		refreshUsers();
		startUserRefreshingTimer();
		refresh(simConfiguration.getCurrentUser());
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
	@Override
	public void refreshUsers() {
		List<User> users = userDao.getUsers();
		processNewOrRemovedUsers(users);
	}

	/**
	 * Aktualisiert den Nutzer, startet einen Timer, der das regelmaessig macht.
	 */
	@Override
	public void refresh(User u) {
		if (!refreshingUsers.contains(u.getName())) {
			Timer t = new Timer("RefreshingUserService: Refresh own user Timer");
			TimerTask task = new TimerTask() {
				@Override
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

			t.schedule(task, 0,
					simConfiguration.getInitRefreshUserStateInterval());

			refreshingUsers.add(u.getName());
		}
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
				currentUsers.addAll(users);
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
				for (UserChangeObserver userChangeListener : userChangeListeners) {
					userChangeListener.onUserRemoved(offlineUsers);
				}
			}
		}

		if (userChangeListeners.size() > 0) {
			if (newUsers.size() > 0) {
				for (UserChangeObserver userChangeListener : userChangeListeners) {
					userChangeListener.onUserAdded(newUsers);
				}
			}
		}
	}

	/**
	 * Fuegt einen {@link UserChangeObserver} ein.
	 *
	 * @param listener
	 *            Listener.
	 */
	@Override
	public void registerUserChangeObserver(UserChangeObserver listener) {
		this.userChangeListeners.add(listener);
	}

	/**
	 * Startet den Thread, der die User liste aktuell haelt.
	 */
	public void startUserRefreshingTimer() {
		if (!userRefreshingTimerStarted) {
			Timer reloadUserTimer = new Timer(
					"RefreshingUserService: Reload Users Timer");

			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					try {
						userDao.purgeOfflineUsers();
						refreshUsers();
					} catch (Exception e) {
						// Sollte eine Exception fliegen, soll der Timer
						// weiterlaufen.
						e.printStackTrace();
					}
				}
			};
			reloadUserTimer.schedule(task, simConfiguration.getUserLoadDelay(),
					simConfiguration.getUserLoadPeriod());

			userRefreshingTimerStarted = true;
		}
	}

	/**
	 * Gibt die aktuellen User zurueck. Falls bisher noch keine USer geladen
	 * wurden, wird eine Liste zurueckgegeben, die bei contains immer true
	 * zurueck gibt. (Da noch nicht klar ist, ob der User vorhanden ist oder
	 * nicht, hacky).
	 *
	 * @return list von usern.
	 */
	@Override
	public List<User> getCurrentUsers() {
		/*
		 * List<User> users = new ArrayList<User>(); if (!usersLoaded) { users =
		 * new ArrayList<User>() { public boolean contains(Object o) { return
		 * true; }; }; } else { users = currentUsers; } return users;
		 */
		return currentUsers;
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

	@Override
	public User getUser(String name) {
		return userDao.getUser(name);
	}

	@Override
	public void joinOrCreateRoom(String username, String roomName) {
		Room r = new Room();
		r.setName(roomName);
		r.setUsers(new ArrayList<User>());
		r.getUsers().add(getUser(username));
		roomDao.saveOrUpdate(r);
	}

}
