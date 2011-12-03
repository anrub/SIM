package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import devhood.im.sim.Sim;
import devhood.im.sim.model.User;
import devhood.im.sim.service.ServiceLocator;
import devhood.im.sim.service.interfaces.RegistryService;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.EventObserver;
import devhood.im.sim.ui.event.Events;

/**
 * Diese Klasse erzeugt das Hauptfenster der Anwendung.
 * 
 * @author flo
 * 
 */
public class MainFrame implements EventObserver {

	/**
	 * Das ist der main frame.
	 */
	private JFrame frame;

	/**
	 * Das ist das Scrollpane, in dem die User stehen.
	 */
	private JScrollPane userScrollPane;

	/**
	 * Das ist das Scrollpane, in dem Messages empfangen, versendet werden.
	 */
	private SendReceiveMessagePanel timelineScrollPane;

	/**
	 * Checkbox ob Nachrichten in Systray angezeigt werden sollen, oder nicht.
	 */
	private JCheckBoxMenuItem systrayMenuItem;

	/**
	 * Das ist der Service zum Zugriff auf Stammdaten, wie zb verfuegbare User.
	 */
	private RegistryService registryService;

	public MainFrame() {
		registryService = ServiceLocator.getInstance().getRegistryService();

		EventDispatcher.add(this);
	}

	/**
	 * Initialises the main frame.
	 */
	public void initMainFrame() {
		frame = new JFrame(Sim.applicationName);

		initMenuBar();
		initUserScrollPane();
		initMsgScrollPane();
		initTray();
		initRefreshUserState();

		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLayout(new BorderLayout());

		// Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				userScrollPane, timelineScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(50, 50);
		userScrollPane.setMinimumSize(minimumSize);
		timelineScrollPane.setMinimumSize(minimumSize);

		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		// 4. Size the frame.
		frame.pack();

		// 5. Show it.
		frame.setVisible(true);

	}

	/**
	 * Aktualisiert den eigenen Nutzerstatus in der DB.
	 */
	protected void initRefreshUserState() {
		Timer t = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				User u = Sim.getUser();
				registryService.refresh(u);
			};
		};

		t.schedule(task, 0, 10000);
	}

	/**
	 * Initialies the user scrollpane.
	 */
	protected void initUserScrollPane() {
		JPanel userPanel = new UserPanel(registryService);

		userScrollPane = new JScrollPane(userPanel);

	}

	/**
	 * Initialies the messsaging scrollpane.
	 */
	protected void initMsgScrollPane() {
		SendReceiveMessagePanel p = new SendReceiveMessagePanel();

		timelineScrollPane = p;
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
		cbMenuItem = new JCheckBoxMenuItem("Statusänderungen anzeigen");
		menuNotifications.add(cbMenuItem);

		systrayMenuItem = new JCheckBoxMenuItem("Nachrichten Systray anzeigen");
		menuNotifications.add(systrayMenuItem);

		cbMenuItem = new JCheckBoxMenuItem(
				"Ungelesene Nachrichten Systray anzeigen");
		menuNotifications.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Status veröffentlichen");
		menuPrivacy.add(cbMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Tippstatus veröffentlichen");
		menuPrivacy.add(cbMenuItem);

		menuBar.add(menuNotifications);
		menuBar.add(menuPrivacy);

		JMenu layout = createLayoutChangingMenu();
		menuBar.add(layout);

		frame.setJMenuBar(menuBar);
	}

	/**
	 * Initialises the system tray functionality.
	 */
	protected void initTray() {
		SystemTrayManager sys = new SystemTrayManager();
		sys.init();

		sys.addMouseListener(new MouseAdapter() {
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

	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.SHOW_FRAME.equals(event)) {
			frame.setVisible(true);
		}
	}

	/**
	 * Erzeugt das Menu zum aendern des layouts.
	 * 
	 * @return layouts.
	 */
	public JMenu createLayoutChangingMenu() {
		JMenu layout = new JMenu("Layout");
		final JMenuItem r1 = new JMenuItem("Windows Layout");
		final JMenuItem r2 = new JMenuItem("Java Layout");

		ActionListener layoutListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					if (e.getSource() == r1) {
						UIManager.setLookAndFeel(UIManager
								.getSystemLookAndFeelClassName());
						SwingUtilities.updateComponentTreeUI(frame);
					}
					if (e.getSource() == r2) {
						UIManager.setLookAndFeel(UIManager
								.getCrossPlatformLookAndFeelClassName());
						SwingUtilities.updateComponentTreeUI(frame);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		r1.addActionListener(layoutListener);
		r2.addActionListener(layoutListener);

		layout.add(r1);
		layout.add(r2);

		return layout;
	}
}