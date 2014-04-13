package devhood.im.sim.ui.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Receiver;
import devhood.im.sim.model.Room;
import devhood.im.sim.service.interfaces.UserService;
import devhood.im.sim.ui.view.JoinRoomView;

@Named
public class JoinRoomPresenter {

	private JoinRoomView view;

	private String roomName;

	@Inject
	private UserService userService;

	public void show(JFrame parent) {
		view = new JoinRoomView(parent);
		view.setLocationRelativeTo(parent);

		view.addButtonMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				joinRoomSubmitAction();
			}

		});

		view.getRoomNameField().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				joinRoomSubmitAction();
			}
		});

		view.pack();
		view.setVisible(true);
	}

	public String getRoomName() {
		return roomName;
	}

	private void joinRoomSubmitAction() {
		roomName = view.getRoomName();

		if (!StringUtils.isEmpty(roomName)) {
			if (roomName.length() > 15) {
				JOptionPane.showMessageDialog(view,
						"Raumnamen dürfen maximal 15 Zeichen lang sein!",
						"Raumname zu lang!", JOptionPane.WARNING_MESSAGE);
			} else {
				joinOrCreateRoomAndFireEvents(roomName, userService
						.getCurrentUser().getName());
				view.dispose();
			}
		}
	}

	private void joinOrCreateRoomAndFireEvents(String roomName, String username) {
		Room room = new Room();
		room.setName(roomName);

		userService.joinOrCreateRoom(username, room);

		fireUiEvents();
	}

	private void fireUiEvents() {
		Receiver r = new Receiver();
		r.setName(roomName);
		r.setRoom(true);

		EventDispatcher.fireEvent(Events.RECEIVER_SELECTED, r);
	}
}
