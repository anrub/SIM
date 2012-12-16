package devhood.im.sim.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.service.interfaces.UserService;

@Named
public class ExitAction implements ActionListener, Action {
	@Inject
	private SimConfiguration simConfiguration;
	@Inject
	private UserService userService;

	/**
	 * Bei click auf Exit, Anwendung schließen.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		execute();

	}

	@Override
	public void execute() {
		try {
			userService.logout(simConfiguration.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
