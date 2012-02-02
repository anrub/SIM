package devhood.im.sim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
		setMinimumSize(new Dimension(500, 400));

		JTabbedPane pane = new JTabbedPane();
		
		JPanel colorPanel = new JPanel(new BorderLayout());
		
		JPanel notificationsPanel = new JPanel();
		JPanel lafPanel = new JPanel();
		JPanel aboutPanel = new JPanel();
		JPanel elsePanel = new JPanel();
		
		JLabel l1 = new JLabel("Wählen Sie die Farbe des eigenen Benutzers:");
		colorPanel.add(l1, BorderLayout.NORTH);
		
		
		Color selectedColor = new Color(simConfiguration.getUserColorRgb()[0],
			simConfiguration.getUserColorRgb()[1],
			simConfiguration.getUserColorRgb()[2]);
	
		final JColorChooser chooser = new JColorChooser(selectedColor);
		chooser.setName("userColor");
		colorPanel.add(chooser,BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		JButton save = new JButton("Speichern");
		
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = chooser.getSelectionModel().getSelectedColor();
				simConfiguration.saveUserColor(c);
			}
		});

		buttonPane.add(save);
		JButton reset = new JButton("Reset");
		
		reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = new Color(0);
				chooser.setColor(c);
				simConfiguration.saveUserColor(c);
			}
		});

		buttonPane.add(reset);
		
		colorPanel.add(buttonPane, BorderLayout.SOUTH);
		
		
		pane.addTab("Farben", colorPanel);
		
		pane.addTab("Benachrichtigungen", notificationsPanel);
		pane.addTab("Look & Feel", lafPanel);
		pane.addTab("About", aboutPanel);
		pane.addTab("Sonstiges", elsePanel);
		
		
		add(pane);

		// pack();
		// setVisible(true);
	}
	
}
