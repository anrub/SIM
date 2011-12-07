package devhood.im.sim.service.mock;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.MessageService;

/**
 * Peer to Peer Implementierung des {@link MessageService}.
 * 
 * @author flo
 * 
 */
public class DummyMessageService implements EventObserver, MessageService {

	private Logger log = Logger.getLogger(DummyMessageService.class.toString());

	/**
	 * Startet Message Server in neuem Thread.
	 * 
	 * @throws IOException
	 *             Exception wenn ServerSocket nicht erzeugt werden kann
	 */
	public DummyMessageService() {
		EventDispatcher.add(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.MESSAGE_SENT.equals(event)) {
			Message m = (Message) o;
			List<String> receiver = m.getReceiver();

			System.out.println("Versuche Message zu senden: " + m);
		} else if (Events.MESSAGE_RECEIVED.equals(event)) {
			System.out.println(event);
		}
	}

	@Override
	public void receiveMessage(Message m) {
		EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, m);
	}

	@Override
	public boolean sendMessage(Message m) {
		EventDispatcher.fireEvent(Events.MESSAGE_SENT, m);
		System.out.println(m);
		return true;
	}

	@Override
	public boolean validateIncomingMessage(Message m) {
		return true;
	}
}
