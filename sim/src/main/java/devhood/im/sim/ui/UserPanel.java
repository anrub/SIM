package devhood.im.sim.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import devhood.im.sim.event.EventDispatcher;
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
public class UserPanel extends JPanel {

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
	 * Fuegt die Users aus registryService in die Liste ein.
	 */
	public void addUsers() {
		List<User> users = registryService.getUsers();
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
						EventDispatcher.fireEvent(Events.USER_SELECTED, box);
					}
				}
			});

			add(userCheckBox);
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

				removeAll();
				addUsers();
				validate();
				repaint();
			}
		};

		t.schedule(task, userLoadDelay, userLoadPeriod);
	}
}
