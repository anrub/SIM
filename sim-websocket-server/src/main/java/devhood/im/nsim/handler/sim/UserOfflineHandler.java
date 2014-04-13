package devhood.im.nsim.handler.sim;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.UserOfflineNotice;
import devhood.im.nsim.util.JacksonEncoder;
import devhood.im.sim.event.Events;

public class UserOfflineHandler implements ISimEventHandler {
	private Broadcaster broadcaster;

	public UserOfflineHandler(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Override
	public void handle(Events e, Object o) {
		UserOfflineNotice n = new UserOfflineNotice();
		n.setText(o.toString());
		JacksonEncoder enc = new JacksonEncoder();
		broadcaster.broadcast(enc.encode(n));
	}
}
