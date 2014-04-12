package devhood.im.nsim.handler.ui;

import java.util.Arrays;

import devhood.im.nsim.model.Message;
import devhood.im.sim.ui.event.EventDispatcher;
import devhood.im.sim.ui.event.Events;

public class SendMessageHandler implements IEventHandler {

	@Override
	public void handle(Message m) {
		devhood.im.sim.messages.model.Message msg = null;
		if ("Stream".equals(m.getReceiver())) {
			msg = new devhood.im.sim.messages.model.BroadcastMessage();
			msg.setText(m.getText());
		} else {
			msg = new devhood.im.sim.messages.model.SingleMessage();
			msg.setText(m.getText());
			msg.setReceiver(Arrays.asList(new String[] { m.getReceiver() }));
		}

		EventDispatcher.fireEvent(Events.SEND_MESSAGE, msg);
	}

}
