package devhood.im.sim.ui.presenter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JOptionPane;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.ui.view.ConfigurationView;

@Named
public class ConfigurationPresenter {

	@Inject
	private ConfigurationView configurationView;

	@Inject
	private SimConfiguration simConfiguration;

	@PostConstruct
	public void init() {
		configurationView.addSaveActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Color c = configurationView.getSelectedColor();
				simConfiguration.saveUserColor(c);
				JOptionPane.showMessageDialog(null, "Farbe wurde gespeichert!",
						"Einstellungen gespeichert",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		configurationView.getAutojoinroomsbutton().addMouseListener(
				new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						simConfiguration.saveAutojoinRooms(configurationView
								.getAutojoinRooms());
					}
				});

	}

	public void show() {
		configurationView.setAutojoinrooms(simConfiguration
				.getAutojoinRooms());

		configurationView.pack();
		configurationView.setVisible(true);
	}

}
