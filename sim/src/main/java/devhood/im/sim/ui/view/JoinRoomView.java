package devhood.im.sim.ui.view;

import java.awt.FlowLayout;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JoinRoomView extends JDialog {
	private JButton button;
	private JTextField roomName;

	public JoinRoomView(JFrame parent) {
		super(parent, "Raumname", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		JPanel p = new JPanel(new FlowLayout());
		JLabel label = new JLabel("Name:");

		roomName = new JTextField(20);
		button = new JButton("Raum öffnen");

		p.add(label);
		p.add(roomName);
		p.add(button);

		add(p);
	}

	public void addButtonMouseListener(MouseListener mouseListener) {
		button.addMouseListener(mouseListener);
	}

	public String getRoomName() {
		return roomName.getText();
	}

	public void setRoomName(JTextField roomName) {
		this.roomName = roomName;
	}

	public JTextField getRoomNameField() {
		return this.roomName;
	}
}
