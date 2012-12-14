package devhood.im.sim.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.inject.Named;
import javax.swing.JFrame;

@Named
public class JoinRoomPresenter {

	private JoinRoomView view;

	private String roomName;

	public void show(JFrame parent) {
		view = new JoinRoomView(parent);

		view.addButtonMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				roomName = view.getRoomName();
				view.dispose();
			}
		});

		view.pack();
		view.setVisible(true);
	}

	public String getRoomName() {
		return roomName;
	}
}
