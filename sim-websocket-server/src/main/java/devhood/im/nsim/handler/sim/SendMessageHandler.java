package devhood.im.nsim.handler.sim;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.SendMessage;
import devhood.im.nsim.util.SimpleBroadcaster;
import devhood.im.sim.messages.model.Message;
import devhood.im.sim.ui.event.Events;

public class SendMessageHandler implements ISimEventHandler {
	private Broadcaster broadcaster;

	public SendMessageHandler(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	@Override
	public void handle(Events e, Object o) {
		Message msg = (Message) o;

		SendMessage m = new SendMessage();
		m.setText(msg.getText());
		m.setSender(msg.getSender());
		if (msg.getReceiver() != null && msg.getReceiver().size() > 0) {
			m.setReceiver(msg.getReceiver().get(0));
		}

		new SimpleBroadcaster(broadcaster).broadcast(m);
	}

}
