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

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.model.Room;
import devhood.im.sim.model.User;
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.repository.RoomRepository;
import devhood.im.sim.repository.UserDao;
import devhood.im.sim.service.interfaces.UserChangeObserver;
import devhood.im.sim.service.interfaces.UserService;

/**
 * Dieser {@link UserService} aktualisiert sich selbst und den uebergebenen User
 * regelmaessg.
 *
 * @author flo
 *
 */
@Named
@Transactional
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
	private RoomRepository roomDao;

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

	@Inject
	private ApplicationContext applicationContext;

	/**
	 * Initialisiert.
	 */
	@PostConstruct
	public void init() {
		refreshUsers();
		startUserRefreshingTimer();
		refresh(getCurrentUser());
	}

	private User currentUser;

	@Override
	public User getCurrentUser() {
		if (currentUser == null) {
			currentUser = getUser(simConfiguration.getUsername());

			if (currentUser == null) {
				currentUser = new User(simConfiguration.getUsername(),
						simConfiguration.getCurrentHostname(),
						simConfiguration.getPort(), new Date(),
						simConfiguration.getKeyPair().getPublic(),
						UserStatus.AVAILABLE.getText());
			}
		} else {
			currentUser.setAddress(simConfiguration.getCurrentHostname());
			currentUser.setPort(simConfiguration.getPort());
			currentUser.setPublicKey(simConfiguration.getKeyPair().getPublic());
			currentUser.setName(simConfiguration.getUsername());
			userDao.save(currentUser);
		}

		if (currentUser.getRooms().size() == 0) {
			Room stream = roomDao.findByName(simConfiguration
					.getStreamTabName());
			if (stream == null) {
				stream = new Room();
			}
			stream.getUsers().add(currentUser);
			stream.setName(simConfiguration.getStreamTabName());

			userDao.save(currentUser);
			currentUser.getRooms().add(stream);
			roomDao.save(stream);
		}

		return currentUser;
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
	public Iterable<User> getUsers() {
		return userDao.findAll();
	}

	/**
	 * Fuegt die Users aus registryService in die Liste ein.
	 */
	@Override
	public void refreshUsers() {
		Iterable<User> users = userDao.findAll();
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
						User u = getUser(simConfiguration.getUsername());
						if (u == null) {
							u = getCurrentUser();
						}

						u.setLastaccess(new Date().getTime());
						userDao.save(u);
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
	public void processNewOrRemovedUsers(Iterable<User> users) {
		List<User> newUsers = new ArrayList<User>();
		List<User> offlineUsers = new ArrayList<User>();
		List<User> usersList = getList(users);

		for (User u : users) {
			if (currentUsers.size() == 0) {
				currentUsers.addAll(usersList);
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
			if (!usersList.contains(user)) {
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

	public List getList(Iterable o) {
		List users = new ArrayList();
		for (Object u : o) {
			users.add(u);
		}

		return users;
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
						UserService s = applicationContext
								.getBean(UserService.class);
						s.purgeOfflineUsers();
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
	@Transactional
	public void purgeOfflineUsers() {
		userDao.purgeOfflineUsers(new Date().getTime() - (60 * 5000 * 1));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logout(String username) {
		userDao.deleteByUsername(username);
	}

	@Override
	public User getUser(String name) {
		return userDao.findByTheUsersName(name);
	}

	@Override
	@Transactional
	public void joinOrCreateRoom(String username, String roomName) {
		Room r = roomDao.findByName(roomName);
		if (r == null) {
			r = new Room();
		}
		r.setName(roomName);
		User user = getUser(username);

		if (!r.getUsers().contains(user)) {
			r.getUsers().add(getUser(username));
			roomDao.save(r);
		}
		if (!user.getRooms().contains(r)) {
			user.getRooms().add(r);
			userDao.save(user);
		}

	}

	@Override
	public List<Room> getRooms() {
		Iterable<Room> roomsIt = roomDao.findAll();
		List<Room> rooms = getList(roomsIt);
		return rooms;
	}

}
