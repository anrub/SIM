package devhood.im.sim.service.interfaces;

import devhood.im.sim.messages.FileSendAcceptMessage;
import devhood.im.sim.messages.FileSendRejectMessage;
import devhood.im.sim.messages.FileSendRequestMessage;
import devhood.im.sim.messages.Message;

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

	/**
	 * Eine Anfrage zum Dateiempfang.
	 * 
	 * @param m
	 *            Message.
	 */
	public void messageFileRequestReceivedCallback(FileSendRequestMessage m);

	/**
	 * Eine Anfrage zum Akzeptieren des Dateiempfang.
	 * 
	 * @param m
	 *            Message.
	 */
	public void messageFileRequestAcceptCallback(FileSendAcceptMessage m);

	/**
	 * Anfrage wurde abgelehnt.
	 * 
	 * @param m
	 *            message.
	 */
	public void messageFileRequestRejectCallback(FileSendRejectMessage m);
}
