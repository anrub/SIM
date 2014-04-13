package devhood.im.nsim.handler.ui;

import java.util.ArrayList;
import java.util.List;

import org.atmosphere.cpr.Broadcaster;

import devhood.im.nsim.model.GetContent;
import devhood.im.nsim.model.GetContentResponse;
import devhood.im.nsim.model.Message;
import devhood.im.nsim.util.JacksonEncoder;
import devhood.im.sim.messages.MessageContext;

public class GetContentHandler implements IUiEventHandler {
	private Broadcaster broadcaster;

	private MessageContext messageContext;

	public GetContentHandler(Broadcaster broadcaster,
			MessageContext messageContext) {
		this.broadcaster = broadcaster;
		this.messageContext = messageContext;
	}

	@Override
	public void handle(Message _m) {
		GetContent gc = (GetContent) _m;

		GetContentResponse response = new GetContentResponse();
		response.setConversation(gc.getConversation());
		List<Message> messages = new ArrayList<Message>();

		List<devhood.im.sim.messages.model.Message> dev = messageContext
				.getHistory(gc.getConversation());
		for (devhood.im.sim.messages.model.Message m : dev) {
			Message msg = new Message();
			if (m.getReceiver() != null && m.getReceiver().size() > 0) {
				msg.setReceiver(m.getReceiver().get(0));
			}
			msg.setSender(m.getSender());
			msg.setText(m.getText());
			messages.add(msg);
		}
		response.setMessages(messages);

		JacksonEncoder enc = new JacksonEncoder();
		String encoded = enc.encode(response);
		broadcaster.broadcast(encoded);
	}
}
