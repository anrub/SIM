package devhood.im.sim.model;

import devhood.im.sim.messages.Message;

/**
 * Klasse zum Transport der Fehlermeldung.
 * 
 * 
 * @author flo
 * 
 */
public class MessagingError {

	/**
	 * Exception.
	 */
	private Exception exception;

	/**
	 * MEssage die fehlerhaft verarbeitet wurde.
	 */
	private Message message;

	public MessagingError() {

	}

	public MessagingError(Exception e, Message m) {
		this.exception = e;
		this.message = m;
	}

	public Exception getException() {
		return exception;
	}

	public void setE(Exception e) {
		this.exception = e;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
}
