package devhood.im.sim.ui.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

@Named
public class InfoView extends JFrame {

	private JTextField hostField = new JTextField();

	private JLabel lastPing = new JLabel();

	@PostConstruct
	public void init() {

		JPanel panel = new JPanel(new MigLayout("Wrap 2"));
		JLabel host = new JLabel("Hostname: ");

		panel.add(host);

		panel.add(hostField);

		JLabel lastOnline = new JLabel("Last Ping: ");

		panel.add(lastOnline);
		panel.add(lastPing);

		add(panel);

	}

	public void setLastonline(long timestamp) {
		Date d = new Date(timestamp);
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String date = df.format(d);

		lastPing.setText(date);
	}

	public JTextField getHostField() {
		return hostField;
	}

	public void setHostField(JTextField hostField) {
		this.hostField = hostField;
	}

	public void setHostname(String currentHostname) {
		hostField.setText(currentHostname);

	}

}
