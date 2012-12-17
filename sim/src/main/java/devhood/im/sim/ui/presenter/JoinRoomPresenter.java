package devhood.im.sim.ui.presenter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import devhood.im.sim.ui.view.JoinRoomView;

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

				if (!StringUtils.isEmpty(roomName)) {
					if (roomName.length() > 15) {
						JOptionPane
								.showMessageDialog(
										view,
										"Raumnamen dürfen maximal 15 Zeichen lang sein!",
										"Raumname zu lang!",
										JOptionPane.WARNING_MESSAGE);
					} else {
						view.dispose();
					}
				}
			}
		});

		view.pack();
		view.setVisible(true);
	}

	public String getRoomName() {
		return roomName;
	}
}
