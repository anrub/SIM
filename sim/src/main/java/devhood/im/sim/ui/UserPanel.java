package devhood.im.sim.ui;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.RegistryService;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.Events;

/**
 * Panel zur Auswahl der User. s
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
	 */
	public void init() {
		setLayout(new GridLayout(10, 1));

		List<User> users = registryService.getUsers();
		for (User user : users) {
			JCheckBox userCheckBox = new JCheckBox(user.getName());
			userCheckBox.setName(user.getId());

			userCheckBox.addItemListener(new ItemListener() {

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
