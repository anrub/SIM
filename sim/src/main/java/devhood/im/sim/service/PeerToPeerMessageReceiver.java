package devhood.im.sim.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import devhood.im.sim.Sim;
import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;

/**
 * Bekommt vom ServerSocket eine neue Verbindung zu einem Client und empfängt
 * eine neue Message
 * 
 * @author Tobi
 * 
 */
public class PeerToPeerMessageReceiver implements Runnable {

	private Logger log = Logger.getLogger(PeerToPeerMessageReceiver.class
			.toString());

	/**
	 * Socket für den Server
	 */
	private Socket clientSocket;

	/**
	 * Initialisiert Worker mit aktueller Verbindung
	 * 
	 * @param clientSocket
	 *            aktuelle Client Socket Connection
	 */
	public PeerToPeerMessageReceiver(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * Empfängt neue Nachricht eines Clients
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
				cipher.init(Cipher.UNWRAP_MODE, Sim.getKeyPair().getPrivate());
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
				EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, message);
			}

		} catch (IOException e) {
			log.log(Level.SEVERE,
					"client socket Verbindung InputStream Fehler", e);
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE,
					"ungültige message empfangen (class not found)", e);
		}
	}

	private boolean validateIncomingMessage(Message m) {
		boolean valid = true;
		if (m == null || m.getReceiver() == null || m.getSender() == null
				|| m.getText() == null) {
			valid = false;
		}
		return valid;
	}
}
