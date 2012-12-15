package devhood.im.sim.ui.event;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.messages.MessageContext;
import devhood.im.sim.messages.model.FileSendAcceptMessage;
import devhood.im.sim.messages.model.FileSendRejectMessage;
import devhood.im.sim.messages.model.FileSendRequestMessage;
import devhood.im.sim.messages.model.Message;
import devhood.im.sim.messages.observer.MessageObserver;

/**
 * Observiert {@link MessageContext}. Sobald eine neue Message eingeht, erzeugt
 * diese Klasse einen Event fuer die UI.
 *
 * @author flo
 *
 */
@Named
public class MessageEventProducer implements MessageObserver {

	@Inject
	private MessageContext messageContext;

	@PostConstruct
	public void init() {
		messageContext.registerMessageObserver(this);
	}

	@Override
	public void onMessage(Message m) {
		System.out.println(m);
		EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, m);
	}

	@Override
	public void onFileSendRequestMessage(FileSendRequestMessage m) {
		EventDispatcher.fireEvent(Events.MESSAGE_FILE_REQUEST_RECEIVED, m);
	}

	@Override
	public void onFileSendAcceptMessage(FileSendAcceptMessage m) {
		EventDispatcher.fireEvent(Events.MESSAGE_FILE_ACCEPT_RECEIVED, m);
	}

	@Override
	public void onFileSendRejectMessage(FileSendRejectMessage m) {
		EventDispatcher.fireEvent(Events.MESSAGE_FILE_REJECT_RECEIVED, m);
	}

}
