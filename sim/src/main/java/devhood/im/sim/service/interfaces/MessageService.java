package devhood.im.sim.service.interfaces;

import devhood.im.sim.model.Message;

/**
 * MessageServices rum um die Message.
 * 
 * @author flo
 * 
 */
@Deprecated
public interface MessageService {
	/**
	 * Versenet die MEssage .
	 * 
	 * @param m
	 *            Message zum versenden.
	 * @return erfolgreich true false
	 */
	public boolean sendMessage(Message m);

	/**
	 * Empfaengt eine Message.
	 * 
	 * @param m
	 *            Message.
	 */
	public void receiveMessage(Message m);

	/**
	 * Validiert die neue nachricht.
	 * 
	 * @param m
	 *            message.
	 */
	public boolean validateIncomingMessage(Message m);
}
