package devhood.im.sim.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.RegistryService;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.Events;

/**
 * Panel zur Auswahl der User.
 * 
 * @author flo
 * 
 */
public class UserPanel extends JPanel {

	/**
	 * RegistryService zum Zugriff auf Stammdaten, zb User.
	 */
	private RegistryService registryService;

	public UserPanel(RegistryService registryService) {
		this.registryService = registryService;
		init();
	}

	/**
	 * Initialisiert, füllt das UserPanel.
	 * 
	 * TODO Hier regelmäßig die Userliste abrufen und die UI aktualisieren.
	 */
	public void init() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);

		List<User> users = registryService.getUsers();
		for (User user : users) {
			JCheckBox userCheckBox = new JCheckBox(user.getName());
			userCheckBox.setName(user.getId());

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
}
