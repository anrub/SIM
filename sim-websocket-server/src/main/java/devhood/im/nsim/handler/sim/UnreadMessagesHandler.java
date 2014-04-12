package devhood.im.nsim.handler.sim;

import java.util.List;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.UnreadMessages;
import devhood.im.sim.ui.event.Events;

public class UnreadMessagesHandler implements ISimEventHandler {
	private Broadcaster broadcaster;

	public UnreadMessagesHandler(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Override
	public void handle(Events e, Object o) {
		UnreadMessages r = new UnreadMessages();
		List<String> names = (List<String>) o;
		r.setOfUsers(names);
		new SimpleBroadcaster(this.broadcaster).broadcast(r);
	}
}
