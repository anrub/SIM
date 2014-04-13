package devhood.im.nsim.handler.ui;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.handler.sim.SimpleBroadcaster;
import devhood.im.nsim.model.ErrorMessage;
import devhood.im.nsim.model.Message;

public class UmbrellaUiHandler implements IUiEventHandler {

	private Broadcaster broadcaster;

	public UmbrellaUiHandler(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Override
	public void handle(Message m) {
		ErrorMessage msg = new ErrorMessage();
		msg.setTitle("Nicht behandelt");
		msg.setText("Die UI Nachricht mit id" + m.getId()
				+ " konnte nicht verarbeitet werden. Typ: " + m.getClass());

		new SimpleBroadcaster(broadcaster).broadcast(msg);
	}

}
