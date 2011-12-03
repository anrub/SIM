package devhood.im.sim.service.interfaces;

import devhood.im.sim.model.Message;

/**
 * MessageServices rum um die Message.
 * 
 * @author flo
 * 
 */
public interface MessageService {
	/**
	 * Versenet die MEssage .
	 * 
	 * @param m
	 *            Message zum versenden.
	 * @return erfolgreich true false
	 */
	public boolean sendMessage(Message m);
}