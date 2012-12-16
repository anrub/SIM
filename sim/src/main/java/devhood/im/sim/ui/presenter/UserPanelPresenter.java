package devhood.im.sim.ui.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.ApplicationContext;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.model.Receiver;
import devhood.im.sim.model.Room;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.UserChangeObserver;
import devhood.im.sim.service.interfaces.UserService;
import devhood.im.sim.ui.action.Action;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.EventObserver;
import devhood.im.sim.ui.event.Events;
import devhood.im.sim.ui.view.UserPanelView;

@Named
public class UserPanelPresenter implements EventObserver, UserChangeObserver {

	/**
	 * RegistryService zum Zugriff auf Stammdaten, zb User.
	 */
	@Inject
	private UserService userService;

	@Inject
	private ApplicationContext applicationContext;

	@Inject
	private SimConfiguration simConfiguration;

	@Inject
	private UserPanelView view;

	private boolean refreshed;

	public UserPanelPresenter() {
		EventDispatcher.add(this);
	}

	@Override
	public void eventReceived(Events event, Object o) {

	}

	@PostConstruct
	public void init() {
		startRefreshUsers();

		userService.registerUserChangeObserver(this);

		view.getOutlookBar().setOnBarSelected(new Action() {
			@Override
			public void execute() {
				Receiver r = new Receiver();
				r.setName(view.getSelectedRoom());
				r.setRoom(true);
				EventDispatcher.fireEvent(Events.RECEIVER_SELECTED, r);
			}
		});

	}

	public void openRoom(String name) {
		Room r = new Room();
		r.setName(name);

		if (!view.hasRoom(r)) {
			view.addRoom(r);
		}
	}

	/**
	 * Gibt die Labels der aktuellen User aus der DB zurueck.
	 */
	public List<User> getCurrentUsers() {
		Iterable<User> users = userService.getUsers();
		List<String> userFromDb = new ArrayList<String>();
		List<User> currentUsers = new ArrayList<User>();

		for (User user : users) {
			userFromDb.add(user.getName());
			currentUsers.add(user);
		}

		return currentUsers;
	}

	/**
	 * Aktualisiert regelmaessig das UserPanel mit den aktuellen Daten aus dem
	 * {@link UserService}.
	 */
	public void startRefreshUsers() {
		Timer t = new Timer("UI: Refresh UserPanel Timer");
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				try {
					refreshUi();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t.schedule(task, simConfiguration.getUserLoadDelay(),
				simConfiguration.getUserLoadPeriod());
	}

	private void refreshUi() {
		List<Room> rooms = userService.getRooms();
		view.refreshUi(rooms);

		refreshed = true;
	}

	@Override
	public void onUserRemoved(List<User> user) {
		EventDispatcher.fireEvent(Events.USER_OFFLINE_NOTICE, user);
		refreshUi();
	}

	@Override
	public void onUserAdded(List<User> user) {
		EventDispatcher.fireEvent(Events.USER_ONLINE_NOTICE, user);
		refreshUi();
	}

	public UserPanelView getView() {
		return view;
	}

}
