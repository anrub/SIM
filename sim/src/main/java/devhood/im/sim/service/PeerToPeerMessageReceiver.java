package devhood.im.sim.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import devhood.im.sim.event.EventDispatcher;
import devhood.im.sim.event.Events;
import devhood.im.sim.model.Message;

/**
 * Bekommt vom ServerSocket eine neue Verbindung zu einem Client und empf�ngt
 * eine neue Message
 * 
 * @author Tobi
 * 
 */
public class PeerToPeerMessageReceiver implements Runnable {

	private Logger log = Logger.getLogger(PeerToPeerMessageReceiver.class
			.toString());

	/**
	 * Socket f�r den Server
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
	 * Empf�ngt neue Nachricht eines Clients
	 */
	@Override
	public void run() {
		try {
			InputStream is = clientSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			Message message = (Message) ois.readObject();
			clientSocket.close();
			if(validateIncomingMessage(message)==false) {
				log.log(Level.SEVERE, "ung�ltige message empfangen (validation)");
			} else {
				EventDispatcher.fireEvent(Events.MESSAGE_RECEIVED, message);
			}
		} catch (IOException e) {
			log.log(Level.SEVERE, "client socket Verbindung InputStream Fehler", e);
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, "ung�ltige message empfangen (class not found)", e);
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
