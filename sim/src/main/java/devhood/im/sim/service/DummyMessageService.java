package devhood.im.sim.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import devhood.im.sim.Sim;
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
			String receiver = m.getReceiver();
			User user = ServiceLocator.getInstance().getRegistryService()
					.getUser(receiver);
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
