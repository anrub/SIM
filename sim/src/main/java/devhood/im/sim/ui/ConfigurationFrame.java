package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import devhood.im.sim.config.SimConfiguration;

/**
 * In diesem Frame koennen verschiedene Konfigs vorgenommen werden.
 * 
 * @author flo
 * 
 */
@Named("configurationFrame")
public class ConfigurationFrame extends JFrame {

	@Inject
	private SimConfiguration simConfiguration;

	public ConfigurationFrame() {
	}

	public void init() {
		setTitle("Einstellungen");

		JTabbedPane pane = new JTabbedPane();


		JPanel notificationsPanel = createNotificationsPanel();
		JPanel colorPanel = createColorPanel();

		JPanel lafPanel = new JPanel();
		JPanel aboutPanel = new JPanel();
		JPanel elsePanel = new JPanel();

		pane.addTab("Farben", colorPanel);

		//pane.addTab("Benachrichtigungen", notificationsPanel);
		//pane.addTab("Look & Feel", lafPanel);
		//pane.addTab("About", aboutPanel);
		
		
		//pane.addTab("Sonstiges", elsePanel);

		add(pane);

		// pack();
		// setSize(new Dimension(450, 350));
		// repaint();
		// setVisible(true);
	}

	public JPanel createNotificationsPanel() {
		JPanel notificationsPanel = new JPanel(new BorderLayout());

		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel,
				BoxLayout.PAGE_AXIS));

		JLabel notificationLabel = new JLabel(
				"Hier können Sie einstellen, wie Sie über eingehende Nachrichten benachrichtigt werden",
				JLabel.CENTER);
		JCheckBox systrayMsgs = new JCheckBox(
				"keine Meldungen im Systray anzeigen");
		JCheckBox streamSystrayMsgs = new JCheckBox(
				"keine Stream Messages im Systray anzeigen");
		JCheckBox statusSystrayMsgs = new JCheckBox(
				"keine Statusmeldungen im Systray anzeigen");

		JPanel unreadTimePane = new JPanel();

		JTextField unreadTime = new JTextField(2);
		JLabel intro = new JLabel("Alle ");
		unreadTimePane.add(intro);
		unreadTimePane.add(unreadTime);
		JLabel unreadLabel = new JLabel(
				" Sekunden auf ungelesene Nachrichten hinweisen.");
		unreadTimePane.add(unreadLabel);

		notificationsPanel.add(notificationLabel, BorderLayout.NORTH);
		checkBoxPanel.add(systrayMsgs);
		checkBoxPanel.add(streamSystrayMsgs);

		checkBoxPanel.add(statusSystrayMsgs);
		checkBoxPanel.add(unreadTimePane);

		notificationsPanel.add(checkBoxPanel, BorderLayout.LINE_START);

		JPanel buttonPane = new JPanel();
		JButton save = new JButton("Speichern");

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		buttonPane.add(save);
		JButton reset = new JButton("Reset");

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		buttonPane.add(reset);

		notificationsPanel.add(buttonPane, BorderLayout.SOUTH);

		return notificationsPanel;
	}

	public JPanel createColorPanel() {
		JPanel colorPanel = new JPanel(new BorderLayout());
		JLabel l1 = new JLabel("Wählen Sie die Farbe des eigenen Benutzers:",
				JLabel.CENTER);
		colorPanel.add(l1, BorderLayout.NORTH);

		Color selectedColor = new Color(simConfiguration.getUserColorRgb()[0],
				simConfiguration.getUserColorRgb()[1],
				simConfiguration.getUserColorRgb()[2]);

		final JColorChooser chooser = new JColorChooser(selectedColor);
		chooser.setName("userColor");
		colorPanel.add(chooser, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		JButton save = new JButton("Speichern");

		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = chooser.getSelectionModel().getSelectedColor();
				simConfiguration.saveUserColor(c);
				JOptionPane.showMessageDialog(null, "Farbe wurde gespeichert!",
						"Einstellungen gespeichert",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		buttonPane.add(save);
		JButton reset = new JButton("Reset");

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = new Color(0);
				chooser.setColor(c);
			}
		});

		buttonPane.add(reset);

		colorPanel.add(buttonPane, BorderLayout.SOUTH);

		return colorPanel;
	}

}
