package devhood.im.nsim.handler.sim;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.Message;
import devhood.im.nsim.util.JacksonEncoder;
import devhood.im.sim.ui.event.Events;

public class UmbrellaHandler implements ISimEventHandler {
	private Broadcaster broadcaster;

	public UmbrellaHandler(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Override
	public void handle(Events e, Object o) {
		Message m = new Message();
		m.setText("Core-Event: " + e + ", Inhalt: " + o.toString());
		m.setSender("SIM");

		JacksonEncoder enc = new JacksonEncoder();

		broadcaster.broadcast(enc.encode(m));
	}
}
