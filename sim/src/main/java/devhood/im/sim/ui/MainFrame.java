package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import devhood.im.sim.SimMain;
import devhood.im.sim.service.ServiceLocator;
import devhood.im.sim.service.interfaces.RegistryService;

/**
 * This is the main frame of the application.
 * 
 * @author flo
 * 
 */
public class MainFrame {

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
	private SendReceiveMessagePanel msgScrollPane;

	/**
	 * Das ist der Service zum Zugriff auf Stammdaten, wie zb verfuegbare User.
	 */
	private RegistryService registryService;

	/**
	 * Icon, dass im Tray angezeigt wird.
	 */
	private String trayIcon = "/images/trayIcon.gif";

	public MainFrame() {
		registryService = ServiceLocator.getInstance().getUserService();
	}

	/**
	 * Initialises the main frame.
	 */
	public void initMainFrame() {
		frame = new JFrame("FrameDemo");

		initMenuBar();
		initUserScrollPane();
		initMsgScrollPane();
		initTray();

		// 2. Optional: What happens when the frame closes?
		// frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		// Create a split pane with the two scroll panes in it.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				userScrollPane, msgScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(50, 50);
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
		JPanel userPanel = new UserPanel(registryService);

		userScrollPane = new JScrollPane(userPanel);

	}

	/**
	 * Initialies the messsaging scrollpane.
	 */
	protected void initMsgScrollPane() {
		SendReceiveMessagePanel p = new SendReceiveMessagePanel();

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
		cbMenuItem = new JCheckBoxMenuItem("Statusänderungen anzeigen");
		menuNotifications.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Another one");
		menuNotifications.add(cbMenuItem);

		cbMenuItem = new JCheckBoxMenuItem("Status veröffentlichen");
		menuPrivacy.add(cbMenuItem);
		cbMenuItem = new JCheckBoxMenuItem("Tippstatus veröffentlichen");
		menuPrivacy.add(cbMenuItem);

		menuBar.add(menuNotifications);
		menuBar.add(menuPrivacy);

		frame.setJMenuBar(menuBar);
	}

	/**
	 * Initialises the system tray functionality.
	 */
	protected void initTray() {
		final SystemTray tray = SystemTray.getSystemTray();

		String iconFilename = SimMain.class.getResource(trayIcon).getFile();
		Image image = Toolkit.getDefaultToolkit().getImage(iconFilename);

		PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(image, "The Tip Text", popup);
		trayIcon.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

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

		MenuItem item = new MenuItem("Exit");
		item.setLabel("Exit");

		item.addActionListener(new ActionListener() {

			/**
			 * Bei click auf Exit, Anwendung schließen.
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);

			}
		});

		popup.add(item);
		try {
			tray.add(trayIcon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
