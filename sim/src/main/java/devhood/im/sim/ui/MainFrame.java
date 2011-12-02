package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import devhood.im.sim.model.User;
import devhood.im.sim.service.ServiceLocator;
import devhood.im.sim.service.interfaces.UserService;

/**
 * This is the main frame of the application.
 * 
 * @author flo
 * 
 */
public class MainFrame {

	private JFrame frame;

	private JScrollPane userScrollPane;
	private Component msgScrollPane;

	private UserService userService;

	public MainFrame() {
		userService = ServiceLocator.getInstance().getUserService();
	}

	/**
	 * Initialises the main frame.
	 */
	public void initFrame() {
		// 1. Create the frame.
		frame = new JFrame("FrameDemo");

		initMenuBar();
		initUserScrollPane();
		initMsgScrollPane();

		// 2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		// Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				userScrollPane, msgScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		userScrollPane.setMinimumSize(minimumSize);
		msgScrollPane.setMinimumSize(minimumSize);

		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		// 4. Size the frame.
		frame.pack();

		// 5. Show it.
		frame.setVisible(true);
	}

	/**
	 * Initialies the user scrollpane.
	 */
	protected void initUserScrollPane() {
		JPanel p = new JPanel(new GridLayout(10, 1));

		userScrollPane = new JScrollPane(p);

		// userScrollPane.setLayout(new Lay
		List<User> users = userService.getUsers();
		for (User user : users) {
			JCheckBox userCheckBox = new JCheckBox(user.getName());
			p.add(userCheckBox);
		}

	}

	/**
	 * Initialies the messsaging scrollpane.
	 */
	protected void initMsgScrollPane() {
		JPanel p = new SendMessagePanel();

		msgScrollPane = p;
	}

	/**
	 * Initialies Menubar.
	 * 
	 * @param frame
	 *            Frame.
	 */
	protected void initMenuBar() {
		// Where the GUI is created:
		JMenuBar menuBar;
		JMenu menuNotifications, menuPrivacy;
		JCheckBoxMenuItem cbMenuItem;

		// Create the menu bar.
		menuBar = new JMenuBar();

		// Build the first menu.
		menuNotifications = new JMenu("Benachrichtigungen");
		menuPrivacy = new JMenu("Privat");

		menuNotifications.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("Statusänderung anzeigen");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		menuNotifications.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_H);
		menuNotifications.add(cbMenuItem);

		menuBar.add(menuNotifications);
		menuBar.add(menuPrivacy);

		frame.setJMenuBar(menuBar);
	}
}
