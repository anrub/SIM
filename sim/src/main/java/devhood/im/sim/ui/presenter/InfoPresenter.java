package devhood.im.sim.ui.presenter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.JFrame;

import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.User;
import devhood.im.sim.ui.event.UiEvent;
import devhood.im.sim.ui.view.InfoView;

@Named
public class InfoPresenter implements EventObserver {
	@Inject
	private InfoView view;

	public InfoPresenter() {
		EventDispatcher.add(this);
	}

	@PostConstruct
	public void init() {
		view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.SHOW_INFO_FRAME.equals(event)) {
			UiEvent<User> e = (UiEvent<User>) o;
			User u = e.getPayload();

			view.setLocationRelativeTo(e.getParent());
			view.setHostname(u.getAddress());
			view.setLastonline(u.getLastaccess());
			view.pack();
			view.setVisible(true);
		}

	}
}
