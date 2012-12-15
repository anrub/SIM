package devhood.im.sim.messages.observer;

import devhood.im.sim.messages.model.FileSendAcceptMessage;
import devhood.im.sim.messages.model.FileSendRejectMessage;
import devhood.im.sim.messages.model.FileSendRequestMessage;
import devhood.im.sim.messages.model.Message;

/**
 * MessageCallback, Callback bei ankunft einer neuen Nachricht.
 *
 * @author flo
 *
 */
public interface MessageObserver {
	/**
	 * Callback wird vom Server verwendet, um neue Nachrichten in das System zu
	 * schleusen.
	 *
	 * @param m
	 *            Message
	 */
	public void onMessage(Message m);

	/**
	 * Eine Anfrage zum Dateiempfang.
	 *
	 * @param m
	 *            Message.
	 */
	public void onFileSendRequestMessage(FileSendRequestMessage m);

	/**
	 * Eine Anfrage zum Akzeptieren des Dateiempfang.
	 *
	 * @param m
	 *            Message.
	 */
	public void onFileSendAcceptMessage(FileSendAcceptMessage m);

	/**
	 * Anfrage wurde abgelehnt.
	 *
	 * @param m
	 *            message.
	 */
	public void onFileSendRejectMessage(FileSendRejectMessage m);
}
