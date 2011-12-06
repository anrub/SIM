package devhood.im.sim.ui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import devhood.im.sim.Sim;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.RegistryService;

/**
 * Panel zur Auswahl der User. Aktualisiert sich selbst via
 * {@link RegistryService}.
 * 
 * @author flo
 * 
 */
public class UserPanel extends JPanel implements EventObserver {

	/**
	 * RegistryService zum Zugriff auf Stammdaten, zb User.
	 */
	private RegistryService registryService;

	/**
	 * Laedt User 5 Sekunden nach dem Start.
	 */
	private int userLoadDelay = 5000;

	/**
	 * Laedt User alle 10 Sekunden.
	 */
	private int userLoadPeriod = 10000;

	/**
	 * Liste der Gruppenchatteilnehmer.
	 */
	private List<String> groupChatUsers = new ArrayList<String>();

	/**
	 * * Liste der vorhandenen Gruppenchatteilnehmer.
	 */
	private List<String> oldGroupChatUsers = new ArrayList<String>();

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

		syncUsers();
		startUserRefreshingTimer();
		EventDispatcher.add(this);
	}

	@Override
	public void eventReceived(Events event, Object o) {

		if (Events.UNSELECT_ALL_USERS.equals(event)) {
			for (Component c : getComponents()) {
				if (c instanceof JCheckBox) {
					JCheckBox b = (JCheckBox) c;
					b.setSelected(false);
				}
			}
			groupChatUsers.clear();
			oldGroupChatUsers.clear();
		}
	}

	/**
	 * Checkt ob weitere Checkboxen angehackt sind, wenn ja, wird ein
	 * Gruppenchat eröffnet oder erweitert.
	 * 
	 * @param box
	 */
	public void userSelected(JCheckBox box) {

		for (Component c : getComponents()) {
			if (c instanceof JCheckBox) {
				JCheckBox b = (JCheckBox) c;
				if (b.isSelected() && !b.getName().equals(Sim.username)) {
					if (!oldGroupChatUsers.contains(b.getName())
							&& groupChatUsers.contains(b.getName())) {
						oldGroupChatUsers.add(b.getName());
					}
					if (!groupChatUsers.contains(b.getName())) {
						groupChatUsers.add(b.getName());
					}
				}
			}
		}
		if (groupChatUsers.size() > 1) {
			GroupChatModel model = new GroupChatModel();
			model.setGroupChatName(getGroupChatName(groupChatUsers));
			model.setOldGroupChatName(getGroupChatName(oldGroupChatUsers));
			model.setGroupChatUsers(groupChatUsers);
			model.setOldGroupChatUsers(oldGroupChatUsers);
			EventDispatcher.fireEvent(Events.GROUP_CHAT, model);

			oldGroupChatUsers.clear();

		} else {
			EventDispatcher.fireEvent(Events.USER_SELECTED, box);
		}

	}

	public void userDeselected(JCheckBox box) {
		// oldGroupChatUsers.remove(box.getName());
		// groupChatUsers.remove(box.getName());
		boolean isAtLeastOneSelected = false;

		for (Component c : getComponents()) {
			if (c instanceof JCheckBox) {
				JCheckBox b = (JCheckBox) c;
				if (b.isSelected()) {
					isAtLeastOneSelected = true;

					//oldGroupChatUsers = new ArrayList<String>(groupChatUsers);

				} else {
					if (groupChatUsers.contains(b.getName())) {
						groupChatUsers.remove(b.getName());
					}
				}
			}
		}
		if (!isAtLeastOneSelected) {
			oldGroupChatUsers.clear();
		}
		if (groupChatUsers.size() > 1) {
			GroupChatModel model = new GroupChatModel();
			model.setGroupChatName(getGroupChatName(groupChatUsers));
			model.setOldGroupChatName(getGroupChatName(oldGroupChatUsers));
			model.setGroupChatUsers(groupChatUsers);
			model.setOldGroupChatUsers(oldGroupChatUsers);
			//EventDispatcher.fireEvent(Events.GROUP_CHAT, model);
			// oldGroupChatUsers.clear();
		}
	}

	/**
	 * Erzeugt einen Namen fuer den Gruppenchat.
	 * 
	 * @param usernames
	 *            List der User
	 * @return Name.
	 */
	public String getGroupChatName(List<String> usernames) {
		List<String> usernamesCopy = new ArrayList<String>(usernames);
		Collections.sort(usernamesCopy);
		String name = "";
		Iterator<String> it = usernamesCopy.iterator();
		while (it.hasNext()) {
			name = name + it.next() + (it.hasNext() ? "," : "");
		}

		return name;
	}

	/**
	 * Synchronisiert das UserPanel mit den Daten von {@link RegistryService}.
	 */
	public void syncUsers() {
		List<User> users = registryService.getUsers();
		List<String> usernames = new ArrayList<String>();
		for (User u : users) {
			usernames.add(u.getName());
		}

		for (Component c : getComponents()) {
			if (c instanceof JCheckBox) {
				JCheckBox box = (JCheckBox) c;
				if (!usernames.contains(box.getName())) {
					remove(c);
				}
			}
		}

		for (User user : users) {
			JCheckBox userCheckBox = new JCheckBox(user.getName());
			userCheckBox.setName(user.getName());

			userCheckBox.addItemListener(new ItemListener() {

				/**
				 * Jede Checkbox bekommt einen Listener, der anschlägt, wenn Sie
				 * ausgewählt wurde. Dabei wird Ein Events.USER_SELECTED
				 * gefeuert.
				 */
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						JCheckBox box = (JCheckBox) e.getSource();
						// EventDispatcher.fireEvent(Events.USER_SELECTED, box);
						userSelected(box);
					} else if (e.getStateChange() == ItemEvent.DESELECTED) {
						JCheckBox box = (JCheckBox) e.getSource();

						userDeselected(box);
					}
				}
			});

			boolean isInList = false;
			for (Component c : getComponents()) {
				if (c instanceof JCheckBox) {
					JCheckBox box = (JCheckBox) c;
					if (c.getName().equals(user.getName())) {
						isInList = true;
						break;
					}

				}
			}
			if (!isInList) {
				add(userCheckBox);
			}

		}
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
				syncUsers();
			}
		};

		t.schedule(task, userLoadDelay, userLoadPeriod);
	}
}
