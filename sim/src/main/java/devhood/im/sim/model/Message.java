package devhood.im.sim.model;

import java.io.Serializable;

/**
 * Eine Message.
 * 
 * @author flo
 * 
 */
public class Message implements Serializable {

	// TODO MessageType vorsehen, damit werden technische messages wie
	// statusmeldungen oder binäre messages usw möglich

	/**
	 * Sender Name.
	 */
	private String sender;

	/**
	 * Receiver Name.
	 */
	private String receiver;

	/**
	 * Text der Nachricht.
	 */
	private String text;

	private String messageId;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String toString() {
		return "Message id: " + messageId + " Sender: " + sender
				+ ", Receiver: " + receiver + ", Text: " + text;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
