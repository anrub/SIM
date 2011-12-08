package devhood.im.sim.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import devhood.im.sim.Sim;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;
import devhood.im.sim.model.MessageType;
import devhood.im.sim.model.MessagingError;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.MessageService;

/**
 * Peer to Peer Implementierung des {@link MessageService}. Dieser Service
 * versendet Nachrichten.
 * 
 * @author Tobi, flo
 * 
 */
public class PeerToPeerMessageSender implements EventObserver, Runnable {

	private Logger log = Logger.getLogger(PeerToPeerMessageSender.class
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
	 * Threadpool für das versenden
	 */
	private Executor threadPool;

	/**
	 * Startet Message Server in neuem Thread.
	 * 
	 * @throws IOException
	 *             Exception wenn ServerSocket nicht erzeugt werden kann
	 */
	public PeerToPeerMessageSender() throws IOException {
		threadPool = Executors.newFixedThreadPool(Sim.senderThreads);
		serverSocket = new ServerSocket(0);
		Sim.setPort(serverSocket.getLocalPort());
		EventDispatcher.fireEvent(Events.SERVER_INITIALISED, null);

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
	public void sendMessageToAllUsers(final Message m) {
		List<User> users = ServiceLocator.getInstance().getRegistryService()
				.getUsers();
		for (final User user : users) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					sendMessage(user, m);
				}
			});
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

			OutputStream os = socket.getOutputStream();

			try {
				// temporaeren AES Key erzeugen
				KeyGenerator keygen = KeyGenerator.getInstance("AES");
				SecureRandom random = new SecureRandom();
				keygen.init(random);
				SecretKey key = keygen.generateKey();

				// mit RSA verschluesseln und an empfaenger senden
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.WRAP_MODE, user.getPublicKey());
				byte[] encryptedAesKey = cipher.wrap(key);
				os.write(encryptedAesKey);

				// eigentliche Nachricht mit AES verschluesseln
				cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				os = new CipherOutputStream(os, cipher);
			} catch (Exception e) {
				log.log(Level.SEVERE, "verschluesselung fehlgeschlagen", e);
			}

			// message object senden
			ObjectOutputStream obs = new ObjectOutputStream(os);
			obs.writeObject(m); 
			obs.flush();
			obs.close();
			socket.close();
		} catch (UnknownHostException e) {
			log.log(Level.SEVERE,
					"message sent - client socket Verbindung Fehler", e);
			EventDispatcher.fireEvent(Events.MESSAGE_SEND_FAILED,
					new MessagingError(e, m));
		} catch (IOException e) {
			log.log(Level.SEVERE, "message sent - IOException: ", e);

			EventDispatcher.fireEvent(Events.MESSAGE_SEND_FAILED,
					new MessagingError(e, m));
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
				Thread worker = new Thread(new PeerToPeerMessageReceiver(
						clientSocket));
				worker.start();
			} catch (IOException e) {
				log.log(Level.SEVERE, "client socket connection error", e);
			}
		}
	}

}
