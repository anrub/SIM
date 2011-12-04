package devhood.im.sim.service;

import java.util.logging.Logger;

import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;
import devhood.im.sim.service.interfaces.MessageService;

/**
 * Peer to Peer Implementierung des {@link MessageService}.
 * 
 * @author flo
 * 
 */
public class PeerToPeerMessageService implements MessageService {

	private Logger log = Logger.getLogger(PeerToPeerMessageService.class
			.toString());

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
		boolean valid = validateIncomingMessage(m);
		if (!valid) {
			throw new IllegalArgumentException(
					"Uebergebene Message ist nicht valide! Message: " + m);
		}

		EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, m);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateIncomingMessage(Message m) {
		boolean valid = true;
		if (m == null || m.getReceiver() == null || m.getSender() == null
				|| m.getText() == null) {
			valid = false;
		}
		return valid;
	}

}
