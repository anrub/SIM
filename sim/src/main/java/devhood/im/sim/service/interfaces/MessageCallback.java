package devhood.im.sim.service.interfaces;

import devhood.im.sim.model.Message;

/**
 * MessageCallback, Callback bei ankunft einer neuen Nachricht.
 * 
 * @author flo
 * 
 */
public interface MessageCallback {
	/**
	 * Callback wird vom Server verwendet, um neue Nachrichten in das System zu
	 * schleusen.
	 * 
	 * @param m
	 *            Message
	 */
	public void messageReceivedCallback(Message m);
}
