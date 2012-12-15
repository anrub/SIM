package devhood.im.sim.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.inject.Named;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.dao.interfaces.UserDao;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.EventObserver;
import devhood.im.sim.event.Events;
import devhood.im.sim.messages.FileSendAcceptMessage;
import devhood.im.sim.messages.FileSendRejectMessage;
import devhood.im.sim.messages.FileSendRequestMessage;
import devhood.im.sim.model.MessagingError;
import devhood.im.sim.model.User;
import devhood.im.sim.service.interfaces.FileMessageHandler;

/**
 * FileMessageHandler der Files sendet und empfaengt.
 *
 * @author flo
 *
 */
@Named
public class P2PFileMessageHandler implements EventObserver, FileMessageHandler {

	private Logger log = Logger.getLogger(P2PFileMessageHandler.class
			.toString());

	/**
	 * Filetransfer: Mapping id->File.
	 */
	private static Map<String, File> idFileMap = new ConcurrentHashMap<String, File>();

	/**
	 * Filetransfer: Mapping Id -> aktueller Progress beim versenden.
	 */
	private static Map<String, Long> idProgressSentMap = new ConcurrentHashMap<String, Long>();

	/**
	 * Filetransfer: Mapping Id -> Filename
	 */
	private static Map<String, String> idFilenameMap = new ConcurrentHashMap<String, String>();

	/**
	 * Filetransfer: Mapping Id -> Empfaenger Obj.
	 */
	private static Map<String, FileReceiver> idReceiverMap = new ConcurrentHashMap<String, FileReceiver>();

	@Inject
	private SimConfiguration simConfiguration;

	@Inject
	private UserDao userDao;

	private P2PMessageSender messageSender;

	@Inject
	public P2PFileMessageHandler(P2PMessageSender messageSender) {
		this();
		this.messageSender = messageSender;
	}

	public P2PFileMessageHandler() {
		EventDispatcher.add(this);
	}

	/**
	 * Filetransfer: Lehnt eine Anfrage ab, sendet eine
	 * {@link FileSendRejectMessage} an username.
	 *
	 * @param id
	 *            des Filetransfers.
	 * @param username
	 *            Empfaenger der Ablehnung.
	 */
	@Override
	public void rejectFileMessage(String id, String username) {
		FileSendRejectMessage m = new FileSendRejectMessage();
		m.setId(id);
		m.setText("Id: " + id + " wurde abgelehnt.");
		m.getReceiver().add(username);
		m.setSender(simConfiguration.getUsername());

		FileReceiver r = idReceiverMap.get(m.getId());
		if (r != null) {
			r.stopped = true;
		}
		idReceiverMap.remove(m.getId());

		User u = userDao.getUser(username);
		messageSender.sendMessage(u, m);
	}

	/**
	 * Filetransfer: Sendet eine Anfrage zum Dateitransfer.
	 *
	 * @param file
	 *            Datei zum versand
	 * @param toUser
	 *            user der die Anfrage bekommt.
	 */
	@Override
	public String sendFileRequest(File file, String toUser) {
		FileSendRequestMessage msg = new FileSendRequestMessage();
		msg.getReceiver().add(toUser);
		msg.setSender(simConfiguration.getUsername());

		msg.setFilename(file.getName());
		msg.setSize(file.length());
		msg.setText(file.getName());

		String id = UUID.randomUUID().toString();
		msg.setId(id);

		User u = userDao.getUser(toUser);
		messageSender.sendMessage(u, msg);

		idFileMap.put(msg.getId(), file);

		return id;
	}

	/**
	 * Filetransfer: Startet den Dateitransfer, oeffnet eine Socket Verbindung
	 * zum vorher ausgehandelten Port und streamt die Datei.
	 *
	 * @param m
	 *            {@link FileSendAcceptMessage} msg.
	 */
	public void startFileSendTransfer(FileSendAcceptMessage m) {
		File file = idFileMap.get(m.getId());
		idFileMap.remove(m.getId());

		if (file != null) {
			int userport = m.getPortToUser();
			Socket socket = null;

			try {
				User user = userDao.getUser(m.getSender());
				String addr = user.getAddress();
				socket = new Socket(addr, userport);

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
				InputStream in = new BufferedInputStream(new FileInputStream(
						file));
				idProgressSentMap.put(m.getId(), 0l);

				byte[] buffer = new byte[100];

				int numRead;
				while ((numRead = in.read(buffer)) >= 0) {
					os.write(buffer, 0, numRead);
					try {
						long before = idProgressSentMap.get(m.getId());
						long after = before + numRead;
						idProgressSentMap.put(m.getId(), after);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				os.flush();
				os.close();
				in.close();

				// ObjectOutputStream obs = new ObjectOutputStream(os);
				// obs.writeObject(m);
				// obs.flush();
				// obs.close();
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
	}

	/**
	 * Filetransfer: Gibt die gesendeten/empfangenen bytes des Transfers mit der
	 * Id zurueck.
	 *
	 * @param id
	 *            des transfers.
	 */
	@Override
	public long getProgress(String id) {
		Long progress = 0l;
		if (id != null) {
			progress = idProgressSentMap.get(id);
			if (progress == null) {
				progress = 0l;
			}
		}
		return progress;
	}

	/**
	 * Filetransfer: Akzeptiert den Filetransfer, sendet eine
	 * {@link FileSendAcceptMessage} an den Anfrager.
	 *
	 * @param username
	 *            username, der die Anfrage sendete
	 * @param id
	 *            des transfers.
	 * @param storeInPath
	 *            Pfad, in dem die Datei abgelegt werden soll.
	 */
	@Override
	public void acceptFileMessage(String username, String id, String storeInPath) {
		FileSendAcceptMessage msg = new FileSendAcceptMessage();
		msg.setId(id);
		msg.setSender(simConfiguration.getUsername());
		msg.getReceiver().add(username);
		msg.setText("Accept: " + id);

		int port = startServerForFileReceive(storeInPath, id);
		msg.setPortToUser(port);

		msg.setText(id);
		User u = userDao.getUser(username);
		messageSender.sendMessage(u, msg);
	}

	/**
	 * Filetransfer: Startet den Server, auf dem die Datei empfangen werden
	 * soll.
	 *
	 * @param storeInPath
	 *            Speichert die Datei in diesem Ordner.
	 * @param id
	 *            Id der Dateiübertragung, die zuvor per
	 *            {@link FileSendRequestMessage} bekannt gemacht wurde
	 * @return Port auf dem nun der Server lauscht.
	 */
	public int startServerForFileReceive(String storeInPath, String id) {
		int port = -1;
		try {
			ServerSocket fileServerSocket = new ServerSocket(0);
			port = fileServerSocket.getLocalPort();

			FileReceiver receiver = new FileReceiver();
			receiver.fileServerSocket = fileServerSocket;
			receiver.storeInPath = storeInPath;
			receiver.id = id;

			Thread t = new Thread(null, receiver, "File Receiving Thread " + id);
			idReceiverMap.put(id, receiver);

			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return port;
	}

	/**
	 * Filetransfer: Emfängt die Datei vom Socket und schreibt sie.
	 *
	 * @author flo
	 *
	 */
	class FileReceiver implements Runnable {

		/**
		 * ServerSockt, von dem die Datei gelesen wird.
		 */
		private ServerSocket fileServerSocket;

		/**
		 * Speichert die Datei in diesem Ordner.
		 */
		private String storeInPath;

		/**
		 * Id der Uebertragung.
		 */
		private String id;

		private boolean stopped = false;

		@Override
		public void run() {
			try {
				Socket clientSocket = fileServerSocket.accept();

				InputStream is = clientSocket.getInputStream();

				try {
					// AES Key lesen
					byte[] wrappedKey = new byte[256];
					is.read(wrappedKey, 0, 256);

					// AES Key mit RSA entschluesseln
					Cipher cipher = Cipher.getInstance("RSA");
					cipher.init(Cipher.UNWRAP_MODE, simConfiguration
							.getKeyPair().getPrivate());
					Key key = cipher.unwrap(wrappedKey, "AES",
							Cipher.SECRET_KEY);

					// daten mit AES entschluesseln
					cipher = Cipher.getInstance("AES");
					cipher.init(Cipher.DECRYPT_MODE, key);

					is = new CipherInputStream(is, cipher);

					InputStream in = new BufferedInputStream(is);
					byte[] buffer = new byte[100];
					int len = -1;
					File file = new File(storeInPath + "/"
							+ idFilenameMap.get(id));
					file.createNewFile();
					OutputStream fos = new FileOutputStream(file);
					fos = new BufferedOutputStream(fos);
					idProgressSentMap.put(id, 0l);

					while ((len = in.read(buffer)) != -1 && !stopped) {
						fos.write(buffer, 0, len);
						try {
							// TODO wie sieht das performance maessig aus?
							long before = idProgressSentMap.get(id);
							long after = before + len;
							idProgressSentMap.put(id, after);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					fos.close();
					in.close();
					clientSocket.close();
					fileServerSocket.close();

				} catch (Exception e) {
					log.log(Level.SEVERE, "verschluesselung fehlgeschlagen", e);
				}

			} catch (IOException e) {
				log.log(Level.SEVERE,
						"client socket Verbindung InputStream Fehler", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eventReceived(Events event, Object o) {
		if (Events.MESSAGE_FILE_ACCEPT_RECEIVED.equals(event)) {
			startFileSendTransfer((FileSendAcceptMessage) o);
		} else if (Events.MESSAGE_FILE_REQUEST_RECEIVED.equals(event)) {
			FileSendRequestMessage msg = (FileSendRequestMessage) o;
			idFilenameMap.put(msg.getId(), msg.getFilename());
		}
	}

}
