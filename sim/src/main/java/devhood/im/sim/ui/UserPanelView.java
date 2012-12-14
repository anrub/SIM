package devhood.im.sim.ui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import devhood.im.sim.service.interfaces.UserService;

/**
 * Panel zur Auswahl der User. Aktualisiert sich selbst via {@link UserService}.
 *
 * @author flo
 *
 */
@Named("userPanel")
public class UserPanelView extends JPanel {

	@PostConstruct
	public void init() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);
	}

	/**
	 * Aktualisiert das UserPanel.
	 */
	public void refreshUi(List<JLabel> userLabels) {
		removeAll();

		for (JLabel userLabel : userLabels) {
			add(userLabel);
		}

		validate();
		repaint();
	}

}
