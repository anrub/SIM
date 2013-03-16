package devhood.im.sim.ui.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.model.Receiver;
import devhood.im.sim.model.Room;
import devhood.im.sim.model.User;
import devhood.im.sim.model.UserStatus;
import devhood.im.sim.ui.SendFileFrame;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.Events;
import devhood.im.sim.ui.event.UiEvent;
import devhood.im.sim.ui.util.JOutlookBar;
import devhood.im.sim.ui.util.UiUtil;

/**
 * Panel zur Auswahl der User.
 *
 * @author flo
 *
 */
@Named("userPanel")
public class UserPanelView extends JPanel implements ApplicationContextAware {

	private JOutlookBar outlookBar = new JOutlookBar();

	private MouseListener sendFileListener;
	private MouseListener selectUserMouseListener;

	private ApplicationContext applicationContext;

	private MouseListener visiblecomponentMouseListener;

	private MouseListener quitChatMouseListener;

	/**
	 * Raum, der nicht verlassen werde kann.
	 */
	private String noQuitPossibleRoom;


	@Inject
	private SimConfiguration simConfiguration;

	@PostConstruct
	public void init() {
		BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(layout);

		JScrollPane scrollPane = new JScrollPane(outlookBar);

		add(scrollPane);
	}

	public void addRoom(final Room room) {
		JPanel users = updateUsers(room, room.getUsers());
		boolean quitPossible = !room.getName().equals(noQuitPossibleRoom);

		final JMenuItem quitChatItem = new JMenuItem("Quit Room");

		JCheckBoxMenuItem autojoinItem = new JCheckBoxMenuItem("Autojoin");

		boolean roomIsAutojoined = simConfiguration.getAutojoinRooms()
				.contains(room.getName());
		if (roomIsAutojoined) {
			autojoinItem.setSelected(true);
		}

		autojoinItem.addItemListener(addToAutojoinListener);

		quitChatItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				EventDispatcher.fireEvent(Events.QUIT_CHAT_ITEM_CLICKED, room);
			}
		});

		final JPopupMenu quitChatPopup = new JPopupMenu();
		quitChatPopup.add(autojoinItem);
		quitChatPopup.add(quitChatItem);

		MouseListener showQuitChatMenuMouseListener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					quitChatPopup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		if (!quitPossible) {
			showQuitChatMenuMouseListener = null;
		}

		boolean userIsInRoom = isUserInRoom(room);

		if (userIsInRoom) {
			outlookBar.addBar(room.getName(), users, UiUtil.createImageIcon(
					"/images/icn_member_ambassador.gif", ""),
					showQuitChatMenuMouseListener);
		} else {
			outlookBar.addBar(room.getName(), users, null,
					showQuitChatMenuMouseListener);
		}

	}

	private boolean isUserInRoom(final Room room) {
		boolean userIsInRoom = false;
		for (User user : room.getUsers()) {
			String name = user.getName();
			String ownUsername = simConfiguration.getUsername();
			if (name.equals(ownUsername)) {
				userIsInRoom = true;
			}
		}
		return userIsInRoom;
	}

	private JPanel updateUsers(Room room, Collection<User> userList) {
		JPanel users = new JPanel();

		users.addMouseListener(visiblecomponentMouseListener);

		if (hasRoom(room)) {
			users = (JPanel) getOutlookBar().getBar(room.getName());
			users.removeAll();
		}

		BoxLayout usersLayout = new BoxLayout(users, BoxLayout.PAGE_AXIS);
		users.setLayout(usersLayout);

		for (User u : userList) {
			if (u != null && u.getName() != null) {
				JLabel userLabel = createUserLabel(u);
				users.add(userLabel);
			}
		}
		return users;
	}

	private String firstBarLabel;

	private ItemListener addToAutojoinListener;

	/**
	 * Aktualisiert das UserPanel.
	 */
	public void refreshUi(List<Room> rooms) {
		LinkedList<Room> orderedRoomList = new LinkedList<Room>();
		for (Room room : rooms) {
			if (firstBarLabel != null && firstBarLabel.equals(room.getName())) {
				orderedRoomList.addFirst(room);
			} else {
				orderedRoomList.add(room);
			}
		}

		List<String> currentRooms = new ArrayList<String>();

		for (Room r : orderedRoomList) {

			if (isAddRoom(r)) {
				if (!hasRoom(r)) {
					addRoom(r);
				} else {
					addRoom(r);
				}
				currentRooms.add(r.getName());
			}
		}

		Set<String> bars = outlookBar.getBars();
		List<String> toRemove = new ArrayList<String>();

		for (String bar : bars) {
			if (!currentRooms.contains(bar)) {
				toRemove.add(bar);
			}
		}

		outlookBar.removeBar(toRemove.toArray(new String[] {}));

		validate();
		repaint();
	}

	/**
	 * Gibt zurueck, ob der Raum hinzugefuegt werden soll, oder nicht.
	 *
	 * @param room
	 * @return true/false.
	 */
	private boolean isAddRoom(Room room) {
		boolean add = true;

		if (simConfiguration.isShowOnlyJoinedRooms() && !isUserInRoom(room)) {
			add = false;
		}

		return add;
	}

	/**
	 * Erzeugt das Label zur Anzeige des Benutzers im UserPanel.
	 *
	 *
	 * param user User.
	 *
	 * @return Label
	 */
	public JLabel createUserLabel(final User user) {
		String label = user.getName();

		final JLabel userLabel = new JLabel(label);

		if ( !user.isValid()) {
			userLabel.setToolTipText("Die Identität des Benutzers konnte nicht sichergestellt werden!");
		}

		final JMenuItem sendFileItem = new JMenuItem("Send File");
		sendFileItem.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				showSendFileFrame(user, userLabel);
			}
		});
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(sendFileItem);

		final JMenuItem infoItem = new JMenuItem("Info");
		infoItem.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				EventDispatcher.fireEvent(Events.SHOW_INFO_FRAME,
						new UiEvent<User>(userLabel, user));
			}
		});

		popupMenu.add(infoItem);

		userLabel.addMouseListener(new SelectUserLabelMouseListener(user
				.getName(), popupMenu));

		userLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		setUserLabelIcon(user, userLabel);
		userLabel.setMinimumSize(new Dimension(100, 20));

		return userLabel;
	}

	private void setUserLabelIcon(final User user, final JLabel userLabel) {
		if (UserStatus.AVAILABLE.equals(user.getStatusType())) {
			userLabel.setIcon(UiUtil.createImageIcon(
					"/images/bullet_ball_glass_green.png", ""));
		} else if (UserStatus.BUSY.equals(user.getStatusType())) {
			userLabel.setIcon(UiUtil.createImageIcon(
					"/images/bullet_ball_glass_yellow.png", ""));
		} else if (UserStatus.NOT_AVAILABLE.equals(user.getStatusType())) {
			userLabel.setIcon(UiUtil.createImageIcon(
					"/images/bullet_ball_glass_red.png", ""));
		}

		if (!user.isValid()) {
			ImageIcon icon = UiUtil
					.createImageIcon("/images/exclamation.png",
							"Die Identität dieses Benutzers konnte nicht sichergestellt werden!");

			userLabel.setIcon(icon);
		}
	}

	public JOutlookBar getOutlookBar() {
		return outlookBar;
	}

	public void setOutlookBar(JOutlookBar outlookBar) {
		this.outlookBar = outlookBar;
	}

	public String getSelectedRoom() {
		String name = getOutlookBar().getSelectedBarName();

		return name;
	}

	public boolean hasRoom(Room room) {
		return getOutlookBar().hasBar(room.getName());
	}

	public void setSelectUserMouseListener(MouseListener mouseAdapter) {
		this.selectUserMouseListener = mouseAdapter;

	}

	public MouseListener getSendFileListener() {
		return sendFileListener;
	}

	public MouseListener getSelectUserMouseListener() {
		return selectUserMouseListener;
	}

	class SelectUserLabelMouseListener extends MouseAdapter {
		private String username;
		private JPopupMenu popupMenu;

		public SelectUserLabelMouseListener(String username,
				JPopupMenu popupMenu) {
			this.username = username;
			this.popupMenu = popupMenu;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			Receiver r = new Receiver();
			r.setName(username);
			EventDispatcher.fireEvent(Events.RECEIVER_SELECTED, r);

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			l.setForeground(Color.RED);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			JLabel l = (JLabel) e.getSource();
			l.setForeground(Color.BLACK);
		}

	}

	private void showSendFileFrame(final User user, final JLabel userLabel) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(userLabel);

		if (returnVal == JFileChooser.CANCEL_OPTION) {
			return;
		}
		File file = fc.getSelectedFile();
		if (file.length() == 0) {
			JOptionPane.showMessageDialog(null,
					"Die Datei hat eine Größe von 0!");
			return;
		}

		SendFileFrame sendFileFrame = applicationContext
				.getBean(SendFileFrame.class);
		sendFileFrame.setFileToSend(file);
		sendFileFrame.setToUser(user.getName());
		sendFileFrame.showFrame();

	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void addVisibleUserComponentMouseListener(MouseAdapter mouseAdapter) {
		this.visiblecomponentMouseListener = mouseAdapter;

	}

	public MouseListener getQuitChatMouseListener() {
		return quitChatMouseListener;
	}

	public void setQuitChatMouseListener(MouseListener quitChatMouseListener) {
		this.quitChatMouseListener = quitChatMouseListener;
	}

	public String getFirstBarLabel() {
		return firstBarLabel;
	}

	public void setFirstBarLabel(String firstBarLabel) {
		this.firstBarLabel = firstBarLabel;
	}

	public String getNoQuitPossibleRoom() {
		return noQuitPossibleRoom;
	}

	public void setNoQuitPossibleRoom(String noQuitPossibleRoom) {
		this.noQuitPossibleRoom = noQuitPossibleRoom;
	}

	public void setAddToAutojoinListener(ItemListener itemListener) {
		this.addToAutojoinListener = itemListener;

	}
}
