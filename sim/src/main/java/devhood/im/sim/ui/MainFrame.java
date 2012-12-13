package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.messages.Message;
import devhood.im.sim.model.Receiver;
import devhood.im.sim.model.User;
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.service.MessageFactory;
import devhood.im.sim.service.SimService;
import devhood.im.sim.service.interfaces.UserService;
import devhood.im.sim.ui.util.UiUtil;

/**
 * Diese Klasse erzeugt das Hauptfenster der Anwendung.
 *
 * @author flo
 *
 */
@Named("mainFrame")
public class MainFrame implements EventObserver {

	@Inject
	private SimService simControl;

	@Inject
	private UserService userService;

	/**
	 * Das ist der main frame.
	 */
	private JFrame frame;

	@Inject
	private SystemTrayManager systemTrayManager;

	/**
	 * Das UserPanel.
	 */
	@Inject
	private UserPanel userPanel;

	/**
	 * Das ist das Scrollpane, in dem die User stehen.
	 */
	private JScrollPane userScrollPane;

	/**
	 * Das ist das Scrollpane, in dem Messages empfangen, versendet werden.
	 */
	@Inject
	private SendReceiveMessagePanel timelinePanel;

	/**
	 * Checkbox ob Nachrichten in Systray angezeigt werden sollen, oder nicht.
	 */
	private JCheckBoxMenuItem systrayMenuItem;

	@Inject
	private SimConfiguration simConfiguration;

	@Inject
	private ConfigurationFrame configurationFrame;

	public MainFrame() {
		EventDispatcher.add(this);
	}

	/**
	 * Initialises the main frame.
	 */
	public void initMainFrame() {
		String version = simConfiguration.getVersion();
		frame = new JFrame(simConfiguration.getApplicationName() + " - "
				+ simConfiguration.getUsername() + " " + version);

		frame.setIconImage(UiUtil.createImage("/images/megaphone-icon-64.png"));

		initMenuBar();
		initUserScrollPane();
		initTray();
		configurationFrame.init();
		// initRefreshUserState();

		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLayout(new BorderLayout());

		// Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				userScrollPane, timelinePanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(50, 50);
		userScrollPane.setMinimumSize(minimumSize);
		timelinePanel.setMinimumSize(minimumSize);

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
		userScrollPane = new JScrollPane(userPanel);
		Dimension preferredSize = new Dimension(100, 200);
		userScrollPane.setPreferredSize(preferredSize);
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

		systrayMenuItem = new JCheckBoxMenuItem("Meldungen im Systray anzeigen");
		systrayMenuItem.setSelected(true);
		systrayMenuItem.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					simConfiguration.setShowSystrayMessages(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					simConfiguration.setShowSystrayMessages(false);
				}
			}
		});

		menuNotifications.add(systrayMenuItem);

		systrayMenuItem = new JCheckBoxMenuItem(
				"Stream Msgs im Systray anzeigen");
		systrayMenuItem.setSelected(true);
		systrayMenuItem.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					simConfiguration.setShowSystrayMessages(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					simConfiguration.setShowSystrayMessages(false);
				}
			}
		});

		menuNotifications.add(systrayMenuItem);

		systrayMenuItem = new JCheckBoxMenuItem(
				"Status Msgs im Systray anzeigen");
		systrayMenuItem.setSelected(true);
		systrayMenuItem.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					simConfiguration.setShowStatusInTray(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					simConfiguration.setShowStatusInTray(false);
				}
			}
		});

		menuNotifications.add(systrayMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Status veroeffentlichen");
		menuPrivacy.add(cbMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Tippstatus veroeffentlichen");
		menuPrivacy.add(cbMenuItem);

		JMenu statusMenu = new JMenu("Status");
		JRadioButtonMenuItem status1 = new JRadioButtonMenuItem(
				UserStatus.AVAILABLE.toString());
		status1.setSelected(true);

		JRadioButtonMenuItem status2 = new JRadioButtonMenuItem(
				UserStatus.BUSY.toString());
		JRadioButtonMenuItem status3 = new JRadioButtonMenuItem(
				UserStatus.NOT_AVAILABLE.toString());

		ButtonGroup g = new ButtonGroup();
		g.add(status1);
		g.add(status2);
		g.add(status3);

		statusMenu.add(status1);
		statusMenu.add(status2);
		statusMenu.add(status3);

		ItemListener statusItemListener = new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					JRadioButtonMenuItem item = (JRadioButtonMenuItem) e
							.getSource();
					String statusText = item.getText();

					UserStatus status = UserStatus.get(statusText);

					simConfiguration.getCurrentUser().setStatusType(status);
					Message statusMessage = MessageFactory.createUserStatusMessage(status);
					statusMessage.setSender(simConfiguration.getCurrentUser()
							.getName());

					List<User> users = userService.getUsers();
					for (User user : users) {
						statusMessage.getReceiver().add(user.getName());
					}

					simControl.sendMessage(statusMessage);

				}
			}
		};

		status1.addItemListener(statusItemListener);
		status2.addItemListener(statusItemListener);
		status3.addItemListener(statusItemListener);

		JMenu aboutMenu = new JMenu("About");
		final JMenuItem githubMenuItem = new JMenuItem("Projekt auf github");
		final JMenuItem smileyOverview = new JMenuItem("Smilies");
		aboutMenu.add(githubMenuItem);
		aboutMenu.add(smileyOverview);

		smileyOverview.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UiUtil.openUrlInBrowser(simConfiguration.getSmileyOverview());
			}
		});

		githubMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UiUtil.openUrlInBrowser(simConfiguration.getProjectGithuburl());
			}
		});

		JMenu configMenu = new JMenu("Optionen");
		JMenuItem configMenuItem = new JMenuItem("Einstellungen",
				simConfiguration.getConfigurationFrameIcon());
		configMenu.add(configMenuItem);

		configMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				configurationFrame.pack();
				configurationFrame.setVisible(true);
			}
		});

		menuBar.add(menuNotifications);
		// menuBar.add(menuPrivacy);

		JMenu layout = createLayoutChangingMenu();
		menuBar.add(layout);

		menuBar.add(statusMenu);

		menuBar.add(configMenu);
		menuBar.add(aboutMenu);

		JMenu roomMenu = new JMenu("Raum");
		roomMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Receiver r = new Receiver();
				r.setName("Test");
				r.setRoom(true);
				EventDispatcher.fireEvent(Events.RECEIVER_SELECTED, r);
			}
		});

		menuBar.add(roomMenu);

		frame.setJMenuBar(menuBar);

	}

	/**
	 * Initialises the system tray functionality.
	 */
	protected void initTray() {
		systemTrayManager.addMouseListener(new MouseAdapter() {
			/**
			 * Macht den Frame nach click auf das TrayIcon wieder sichtbar.
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if (MouseEvent.BUTTON1 == e.getButton()) {
					frame.setVisible(true);
				}

			}

		});
	}

	/**
	 * Empfaengt Events von {@link EventDispatcher}.
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.SHOW_FRAME.equals(event)) {
			frame.setVisible(true);
			frame.requestFocusInWindow();
		}
	}

	/**
	 * Erzeugt das Menu zum aendern des layouts.
	 *
	 * @return layouts.
	 */
	public JMenu createLayoutChangingMenu() {
		JMenu layout = new JMenu("Look & Feel");
		ButtonGroup group = new ButtonGroup();

		final JRadioButtonMenuItem r1 = new JRadioButtonMenuItem(
				"Windows Layout");
		r1.setSelected(true);

		final JRadioButtonMenuItem r2 = new JRadioButtonMenuItem("Java Layout");
		final JRadioButtonMenuItem r3 = new JRadioButtonMenuItem(
				"Nimbus Layout");

		final JRadioButtonMenuItem r4 = new JRadioButtonMenuItem("Motif Layout");
		group.add(r1);
		group.add(r2);
		group.add(r3);
		group.add(r4);

		ItemListener layoutListener = new ItemListener() {

			@Override
			public void itemStateChanged(java.awt.event.ItemEvent e) {

				Object obj = e.getItemSelectable();
				if (e.getStateChange() == ItemEvent.SELECTED) {
					try {
						if (obj == r1) {
							UIManager.setLookAndFeel(UIManager
									.getSystemLookAndFeelClassName());
							SwingUtilities.updateComponentTreeUI(frame);
						}
						if (obj == r2) {
							UIManager.setLookAndFeel(UIManager
									.getCrossPlatformLookAndFeelClassName());
							SwingUtilities.updateComponentTreeUI(frame);
						}
						if (obj == r3) {
							UIManager
									.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
							SwingUtilities.updateComponentTreeUI(frame);
						}
						if (obj == r4) {
							UIManager
									.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
							SwingUtilities.updateComponentTreeUI(frame);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};
		r1.addItemListener(layoutListener);
		r2.addItemListener(layoutListener);
		r3.addItemListener(layoutListener);
		r4.addItemListener(layoutListener);

		layout.add(r1);
		layout.add(r2);
		layout.add(r3);
		layout.add(r4);

		return layout;
	}
}