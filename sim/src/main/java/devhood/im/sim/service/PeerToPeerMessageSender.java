package devhood.im.sim.service;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.ApplicationContext;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.dao.interfaces.UserDao;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;
import devhood.im.sim.model.MessageType;
import devhood.im.sim.model.MessagingError;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.MessageCallback;
import devhood.im.sim.service.interfaces.MessageSender;

/**
 * Peer to Peer Implementierung des {@link MessageService}. Dieser Service
 * versendet Nachrichten.
 * 
 * @author Tobi, flo
 * 
 */
@Named("peerToPeerMessageSender")
public class PeerToPeerMessageSender implements EventObserver, Runnable,
		MessageSender {

	private Logger log = Logger.getLogger(PeerToPeerMessageSender.class
			.toString());

	@Inject
	private PeerToPeerMessageReceiver messageReceiver;

	@Inject
	private UserDao userDao;

	@Inject
	private ApplicationContext context;

	@Inject
	private SimConfiguration simConfiguration;

	/**
	 * Socket f�r den Server
	 */
	private ServerSocket serverSocket;

	/**
	 * Server Thread
	 */
	private Thread thread;

	/**
	 * Threadpool f�r das versenden
	 */
	private Executor threadPool;

	private MessageCallback messageCallback;

	/**
	 * Startet Message Server in neuem Thread.
	 * 
	 * @throws IOException
	 *             Exception wenn ServerSocket nicht erzeugt werden kann
	 */

	public void init() throws IOException {
		threadPool = Executors.newFixedThreadPool(simConfiguration
				.getSenderThreads());
		serverSocket = new ServerSocket(0);
		simConfiguration.setPort(serverSocket.getLocalPort());

		EventDispatcher.fireEvent(Events.SERVER_INITIALISED, null);

		thread = new Thread(this);
		thread.start();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.LOGOUT.equals(event)) {
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
		List<User> users = userDao.getUsers();
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
	 * Startet Server und empf�ngt neue Nachrichten
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Socket clientSocket = serverSocket.accept();
				PeerToPeerMessageReceiver msgRec = (PeerToPeerMessageReceiver) context
						.getBean("peerToPeerMessageReceiver");
				msgRec.setClientSocket(clientSocket);
				msgRec.setMessageCallback(messageCallback);
				Thread worker = new Thread(msgRec);
				worker.start();
			} catch (IOException e) {
				log.log(Level.SEVERE, "client socket connection error", e);
			}
		}
	}

	public MessageCallback getMessageCallback() {
		return messageCallback;
	}

	public void setMessageCallback(MessageCallback messageCallback) {
		this.messageCallback = messageCallback;
	}

}
