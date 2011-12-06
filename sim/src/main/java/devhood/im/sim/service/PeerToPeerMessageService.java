package devhood.im.sim.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import devhood.im.sim.Sim;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;
import devhood.im.sim.model.MessageType;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.MessageService;

/**
 * Peer to Peer Implementierung des {@link MessageService}. Dieser Service
 * versendet Nachrichten.
 * 
 * @author Tobi, flo
 * 
 */
public class PeerToPeerMessageService implements EventObserver, Runnable {

	private Logger log = Logger.getLogger(PeerToPeerMessageService.class
			.toString());

	/**
	 * Socket für den Server
	 */
	private ServerSocket serverSocket;

	/**
	 * Server Thread
	 */
	private Thread thread;

	/**
	 * Startet Message Server in neuem Thread.
	 * 
	 * @throws IOException
	 *             Exception wenn ServerSocket nicht erzeugt werden kann
	 */
	public PeerToPeerMessageService() throws IOException {
		serverSocket = new ServerSocket(0);
		Sim.setPort(serverSocket.getLocalPort());
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.MESSAGE_SENT.equals(event)) {
			Message m = (Message) o;
			List<String> receiver = m.getReceiver();
			if (MessageType.SINGLE.equals(m.getMessageType())) {
				if (receiver.size() == 1) {
					String singleReceiver = receiver.get(0);
					User user = ServiceLocator.getInstance()
							.getRegistryService().getUser(singleReceiver);
					sendMessage(user, m);
				}
			} else if (MessageType.ALL.equals(m.getMessageType())) {
				sendMessageToAllUsers(m);
			}

		} else if (Events.LOGOUT.equals(event)) {
			thread.interrupt();
		}
	}

	/**
	 * Schickt die Message an alle bekannten User.
	 * 
	 * @param m
	 *            Message.
	 */
	public void sendMessageToAllUsers(Message m) {
		List<User> users = ServiceLocator.getInstance().getRegistryService()
				.getUsers();
		for (User user : users) {
			sendMessage(user, m);
		}
	}

	/**
	 * Schickt die Nachricht an den User.
	 * 
	 * @param user
	 *            User
	 * @param m
	 *            Message
	 */
	public void sendMessage(User user, Message m) {

		Socket socket = null;
		try {
			socket = new Socket(user.getAddress(), user.getPort());
			ObjectOutputStream os = new ObjectOutputStream(
					socket.getOutputStream());
			os.writeObject(m);
			socket.close();
		} catch (UnknownHostException e) {
			log.log(Level.SEVERE,
					"message sent - client socket Verbindung Fehler", e);
		} catch (IOException e) {
			log.log(Level.SEVERE, "message sent - IOException: ", e);
		}

	}

	/**
	 * Startet Server und empfängt neue Nachrichten
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Socket clientSocket = serverSocket.accept();
				Thread worker = new Thread(
						new PeerToPeerMessageServiceReceiver(clientSocket));
				worker.start();
			} catch (IOException e) {
				log.log(Level.SEVERE, "client socket connection error", e);
			}
		}
	}

}
