package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.ui.action.UpdateUserStatusAction;
import devhood.im.sim.ui.util.UiUtil;

/**
 * Diese Klasse erzeugt das Hauptfenster der Anwendung.
 *
 * @author flo
 *
 */
@Named("mainFrame")
public class MainView {

	/**
	 * Das ist der main frame.
	 */
	private JFrame frame;

	private JMenu statusMenu;

	/**
	 * Das UserPanel.
	 */
	private UserPanelView userPanel;

	/**
	 * Das ist das Scrollpane, in dem die User stehen.
	 */
	private JScrollPane userScrollPane;

	/**
	 * Das ist das Scrollpane, in dem Messages empfangen, versendet werden.
	 */
	@Inject
	private SendReceiveMessagePanel timelinePanel;

	@Inject
	private SimConfiguration simConfiguration;

	private JMenuItem configMenuItem;

	private JMenuItem openRoom;

	public void addOpenRoomMouseListener(ActionListener actionListener) {
		openRoom.addActionListener(actionListener);
	}

	/**
	 * Initialises the main frame.
	 */
	public void initMainFrame(String title) {
		frame = new JFrame(title);

		frame.setIconImage(UiUtil.createImage("/images/megaphone-icon-64.png"));

		initMenuBar();
		initUserScrollPane();
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

		JCheckBoxMenuItem systrayMenuItem = new JCheckBoxMenuItem(
				"Meldungen im Systray anzeigen");
		systrayMenuItem.setSelected(true);
		addSystrayItemListener(systrayMenuItem);

		menuNotifications.add(systrayMenuItem);

		systrayMenuItem = new JCheckBoxMenuItem(
				"Stream Msgs im Systray anzeigen");
		systrayMenuItem.setSelected(true);
		addSystrayItemListenerShowStreamMsgs(systrayMenuItem);

		menuNotifications.add(systrayMenuItem);

		systrayMenuItem = new JCheckBoxMenuItem(
				"Status Msgs im Systray anzeigen");
		systrayMenuItem.setSelected(true);
		addSystrayItemListenerShowStatusMsgs(systrayMenuItem);

		menuNotifications.add(systrayMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Status veroeffentlichen");
		menuPrivacy.add(cbMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Tippstatus veroeffentlichen");
		menuPrivacy.add(cbMenuItem);

		statusMenu = new JMenu("Status");
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

		addStatusItemListener(status1, status2, status3);

		JMenu aboutMenu = new JMenu("About");
		final JMenuItem githubMenuItem = new JMenuItem("Projekt auf github");
		final JMenuItem smileyOverview = new JMenuItem("Smilies");
		aboutMenu.add(githubMenuItem);
		aboutMenu.add(smileyOverview);

		addSmileyOverviewActionListener(smileyOverview);
		addGithubActionListener(githubMenuItem);

		JMenu configMenu = new JMenu("Optionen");
		configMenuItem = new JMenuItem("Einstellungen",
				simConfiguration.getConfigurationFrameIcon());
		configMenu.add(configMenuItem);

		menuBar.add(menuNotifications);
		// menuBar.add(menuPrivacy);

		JMenu layout = createLayoutChangingMenu();
		menuBar.add(layout);

		menuBar.add(statusMenu);

		menuBar.add(configMenu);
		menuBar.add(aboutMenu);

		JMenu roomMenu = new JMenu("Raum");
		openRoom = new JMenuItem("Raum öffnen");
		roomMenu.add(openRoom);

		menuBar.add(roomMenu);

		frame.setJMenuBar(menuBar);

	}

	public void addConfigMenuActionListener(ActionListener actionListener) {
		configMenuItem.addActionListener(actionListener);
	}

	public void addGithubActionListener(final JMenuItem githubMenuItem) {
		githubMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UiUtil.openUrlInBrowser(simConfiguration.getProjectGithuburl());
			}
		});
	}

	public void addSmileyOverviewActionListener(final JMenuItem smileyOverview) {
		smileyOverview.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				UiUtil.openUrlInBrowser(simConfiguration.getSmileyOverview());
			}
		});
	}

	public void addSystrayItemListenerShowStatusMsgs(
			JCheckBoxMenuItem systrayMenuItem) {
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
	}

	public void addSystrayItemListenerShowStreamMsgs(
			JCheckBoxMenuItem systrayMenuItem) {
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
	}

	public void addSystrayItemListener(JCheckBoxMenuItem systrayMenuItem) {
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
	}

	@Inject
	private UpdateUserStatusAction updateUserStatusAction;

	public void addStatusItemListener(JRadioButtonMenuItem... items) {
		for (JRadioButtonMenuItem item : items) {
			ItemListener statusItemListener = new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						JRadioButtonMenuItem item = (JRadioButtonMenuItem) e
								.getSource();
						String statusText = item.getText();
						updateUserStatusAction.setStatusText(statusText);
						updateUserStatusAction.execute();
					}
				}
			};

			item.addItemListener(statusItemListener);
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

	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	public void focus() {
		frame.requestFocusInWindow();
	}

	public void setUserPanel(UserPanelView userPanel) {
		this.userPanel = userPanel;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}