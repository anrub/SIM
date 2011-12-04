package devhood.im.sim.service;

import java.util.logging.Logger;

import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;
import devhood.im.sim.service.interfaces.MessageService;

public class DummyMessageService implements MessageService {

	private Logger log = Logger.getLogger(DummyMessageService.class.toString());

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sendMessage(Message m) {
		log.info("Versuche Msg zu senden: " + m);

		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveMessage(Message m) {
		EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, m);
	}

}
