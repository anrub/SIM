package devhood.im.nsim.handler.sim;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.NewMessage;
import devhood.im.nsim.util.SimpleBroadcaster;
import devhood.im.sim.ui.event.Events;

/**
 * Verarbeitet Eingang einer neuen Nachricht durch SIM core. Transformiert und
 * pusht zur UI.
 * 
 * @author flo
 * 
 */
public class NewMessageHandler implements ISimEventHandler {
	private Broadcaster broadcaster;

	public NewMessageHandler(Broadcaster broadcaster) {
		this.broadcaster = broadcaster;
	}

	public void handle(Events e, Object o) {
		devhood.im.sim.messages.model.Message simMsg = (devhood.im.sim.messages.model.Message) o;

		NewMessage newMsg = new NewMessage();
		newMsg.setReceiver(simMsg.getReceiver().get(0));
		newMsg.setSender(simMsg.getSender());
		newMsg.setText(simMsg.getText());

		new SimpleBroadcaster(broadcaster).broadcast(newMsg);
	}
}
