package devhood.im.sim.ui.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import devhood.im.sim.ui.JOutlookBar;

/**
 * Panel zur Auswahl der User.
 *
 * @author flo
 *
 */
@Named("userPanel")
public class UserPanelView extends JPanel {

	private JOutlookBar outlookBar = new JOutlookBar();

	private JPanel users = new JPanel();

	@PostConstruct
	public void init() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);

		BoxLayout usersLayout = new BoxLayout(users, BoxLayout.PAGE_AXIS);
		users.setLayout(usersLayout);

		outlookBar.addBar("Stream", users);

		outlookBar.addBar("#Webteam", users);

		JScrollPane scrollPane = new JScrollPane(outlookBar);

		add(scrollPane);
	}

	private Map<String, JPanel> converationNameComponentMap = new HashMap<String, JPanel>();

	public void addConversation(String name) {
		JPanel component = new JPanel();
		BoxLayout usersLayout = new BoxLayout(component, BoxLayout.PAGE_AXIS);
		component.setLayout(usersLayout);

		outlookBar.addBar(name, component);

		converationNameComponentMap.put(name, component);
	}

	/**
	 * Aktualisiert das UserPanel.
	 */
	public void refreshUi(List<JLabel> userLabels) {
		users.removeAll();

		for (JLabel userLabel : userLabels) {
			users.add(userLabel);
		}

		users.validate();
		users.repaint();
	}

}
