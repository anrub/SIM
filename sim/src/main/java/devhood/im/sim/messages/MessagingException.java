package devhood.im.sim.messages;

import devhood.im.sim.messages.model.Message;

/**
 * Klasse zum Transport der Fehlermeldung.
 *
 *
 * @author flo
 *
 */
public class MessagingException extends Exception {

	/**
	 * MEssage die fehlerhaft verarbeitet wurde.
	 */
	private Message message;

	public MessagingException() {

	}

	public MessagingException(Throwable cause, Message m) {
		super(cause);
		this.message = m;
	}

	public Message getFailedMessage() {
		return message;
	}

}
