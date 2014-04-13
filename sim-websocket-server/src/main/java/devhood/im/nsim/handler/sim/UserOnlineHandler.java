package devhood.im.nsim.handler.sim;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.UserOnlineNotice;
import devhood.im.nsim.util.JacksonEncoder;
import devhood.im.sim.event.Events;

public class UserOnlineHandler implements ISimEventHandler {
	private Broadcaster broadcaster;

	public UserOnlineHandler(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Override
	public void handle(Events e, Object o) {
		UserOnlineNotice n = new UserOnlineNotice();
		n.setText(o.toString());
		JacksonEncoder enc = new JacksonEncoder();
		broadcaster.broadcast(enc.encode(n));
	}
}
