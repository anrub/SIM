package devhood.im.sim.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Scope;

import devhood.im.sim.config.SimConfiguration;
import devhood.im.sim.messages.FileSendAcceptMessage;
import devhood.im.sim.messages.FileSendRejectMessage;
import devhood.im.sim.messages.FileSendRequestMessage;
import devhood.im.sim.messages.Message;
import devhood.im.sim.service.interfaces.MessageCallback;

/**
 * Bekommt vom ServerSocket eine neue Verbindung zu einem Client und empfängt
 * eine neue Message
 * 
 * @author Tobi
 * 
 */
@Named("peerToPeerMessageReceiver")
@Scope("prototype")
public class PeerToPeerMessageReceiver implements Runnable {

	private Logger log = Logger.getLogger(PeerToPeerMessageReceiver.class
			.toString());

	/**
	 * Socket für den Server.
	 */
	private Socket clientSocket;

	/**
	 * Callback bei eingehender Nachricht.
	 */
	private MessageCallback messageCallback;

	@Inject
	private SimConfiguration simConfiguration;

	public PeerToPeerMessageReceiver() {

	}

	/**
	 * Initialisiert Worker mit aktueller Verbindung
	 * 
	 * @param clientSocket
	 *            aktuelle Client Socket Connection
	 */
	public PeerToPeerMessageReceiver(Socket clientSocket,
			MessageCallback messageCallback) {
		this.clientSocket = clientSocket;
		this.messageCallback = messageCallback;
	}

	/**
	 * Empfängt neue Nachricht eines Clients und ruft den
	 * {@link MessageCallback} auf.
	 */
	@Override
	public void run() {
		try {
			InputStream is = clientSocket.getInputStream();

			try {
				// AES Key lesen
				byte[] wrappedKey = new byte[256];
				is.read(wrappedKey, 0, 256);

				// AES Key mit RSA entschluesseln
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.UNWRAP_MODE, simConfiguration.getKeyPair()
						.getPrivate());
				Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

				// daten mit AES entschluesseln
				cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, key);

				is = new CipherInputStream(is, cipher);
			} catch (Exception e) {
				log.log(Level.SEVERE, "verschluesselung fehlgeschlagen", e);
			}

			// message object empfangen
			ObjectInputStream ois = new ObjectInputStream(is);
			Message message = (Message) ois.readObject();
			clientSocket.close();
			if (validateIncomingMessage(message) == false) {
				log.log(Level.SEVERE,
						"ungültige message empfangen (validation)");
			} else {
				if (message instanceof FileSendRequestMessage) {
					messageCallback
							.messageFileRequestReceivedCallback((FileSendRequestMessage) message);
				} else if (message instanceof FileSendAcceptMessage) {
					messageCallback
							.messageFileRequestAcceptCallback((FileSendAcceptMessage) message);
				} else if (message instanceof FileSendRejectMessage) {
					messageCallback
							.messageFileRequestRejectCallback((FileSendRejectMessage) message);
				} else if (message instanceof Message) {
					messageCallback.messageReceivedCallback(message);
				}
			}

		} catch (IOException e) {
			log.log(Level.SEVERE,
					"client socket Verbindung InputStream Fehler", e);
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE,
					"ung�ltige message empfangen (class not found)", e);
		}
	}

	private boolean validateIncomingMessage(Message m) {
		boolean valid = true;
		if (m == null || m.getReceiver() == null || m.getSender() == null) {
			valid = false;
		}
		return valid;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public MessageCallback getMessageCallback() {
		return messageCallback;
	}

	public void setMessageCallback(MessageCallback messageCallback) {
		this.messageCallback = messageCallback;
	}
}
