package devhood.im.sim.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JOptionPane;

import devhood.im.sim.config.SimConfiguration;

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
	}

	public void show() {
		configurationView.pack();
		configurationView.setVisible(true);
	}

}
