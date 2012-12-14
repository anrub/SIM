package devhood.im.sim.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;

@Named
public class MainPresenter implements EventObserver {

	@Inject
	private MainView mainView;

	@Inject
	private SystemTrayManager systemTrayManager;

	@Inject
	private SimConfiguration simConfiguration;

	@Inject
	private ConfigurationPresenter configurationPresenter;

	@Inject
	private UserPanelPresenter userPanelPresenter;

	public MainPresenter() {
		EventDispatcher.add(this);
	}

	public void init() {
		String title = simConfiguration.getApplicationName() + " - "
				+ simConfiguration.getUsername() + " "
				+ simConfiguration.getVersion();
		mainView.initMainFrame(title);

		addSystemtrayMouseListener();

		mainView.addConfigMenuActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				configurationPresenter.show();
			}
		});

		mainView.setUserPanel(userPanelPresenter.getView());
	}

	/**
	 * Initialises the system tray functionality.
	 */
	protected void addSystemtrayMouseListener() {
		systemTrayManager.addMouseListener(new MouseAdapter() {
			/**
			 * Macht den Frame nach click auf das TrayIcon wieder sichtbar.
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				if (MouseEvent.BUTTON1 == e.getButton()) {
					mainView.setVisible(true);
				}

			}

		});
	}

	/**
	 * Empfaengt Events von {@link EventDispatcher}.
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.SHOW_FRAME.equals(event)) {
			mainView.setVisible(true);
			mainView.focus();
		}
	}
}
