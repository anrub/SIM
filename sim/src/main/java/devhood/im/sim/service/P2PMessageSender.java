package devhood.im.sim.service;

import java.io.BufferedOutputStream;
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
import devhood.im.sim.dao.interfaces.RoomDao;
import devhood.im.sim.dao.interfaces.UserDao;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.messages.Message;
import devhood.im.sim.messages.RoomMessage;
import devhood.im.sim.model.MessagingError;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.MessageCallback;
import devhood.im.sim.service.interfaces.TextMessageSender;

/**
 * Peer to Peer Implementierung des {@link MessageService}. Dieser Service
 * versendet Nachrichten.
 *
 * TODO: Die Crypto sachen besser zusammenfassen, ausmisten was durch den
 * fileversand dazukam. Kann Filetransfer evtl abgekapselt werden?
 *
 * @author Tobi, flo
 *
 */
@Named
public class P2PMessageSender implements EventObserver, Runnable, TextMessageSender {

	private Logger log = Logger.getLogger(P2PMessageSender.class.toString());

	@Inject
	private RoomDao roomDao;

	@Inject
	private UserDao userDao;

	@Inject
	private ApplicationContext context;

	@Inject
	private SimConfiguration simConfiguration;

	/**
	 * Socket für den Server.
	 */
	private ServerSocket serverSocket;

	/**
	 * Server Thread
	 */
	private Thread thread;

	/**
	 * Threadpool für das versenden.
	 */
	private Executor threadPool;

	/**
	 * Callback bei empfangener Nachricht.
	 */
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

		thread = new Thread(null, this,
				"Sys: Message Receiving Thread - initial.");
		thread.start();

		EventDispatcher.add(this);
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
	@Override
	public void sendMessageToAllUsers(final Message m) {
		List<User> users = userDao.getUsers();

		sendMessage(m, users);
	}

	@Override
	public void sendMessageToRoom(final RoomMessage m) {
		List<User> users = roomDao.getUsers(m.getRoomName());

		sendMessage(m, users);
	}

	private void sendMessage(final Message m, List<User> users) {
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
	@Override
	public void sendMessage(User user, Message m) {
		if (user.getName().equals(m.getSender())) {
			return;
		}

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
			os = new BufferedOutputStream(os);
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
	 * Startet Server und empfängt neue Nachrichten.
	 */
	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Socket clientSocket = serverSocket.accept();
				P2PMessageReceiver msgRec = (P2PMessageReceiver) context
						.getBean("peerToPeerMessageReceiver");
				msgRec.setClientSocket(clientSocket);
				msgRec.setMessageCallback(messageCallback);
				Thread worker = new Thread(null, msgRec,
						"Sys: Message Receiving Thread");
				worker.start();
			} catch (IOException e) {
				log.log(Level.SEVERE, "client socket connection error", e);
			}
		}
	}

	public MessageCallback getMessageCallback() {
		return messageCallback;
	}

	@Override
	public void setMessageCallback(MessageCallback messageCallback) {
		this.messageCallback = messageCallback;
	}

}
