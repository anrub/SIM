package devhood.im.sim.model;

import java.io.Serializable;
import java.util.List;

/**
 * Eine Message.
 * 
 * @author flo
 * 
 */
public class Message implements Serializable {
	/**
	 * Sender Name.
	 */
	private String sender;

	/**
	 * Receiver Name.
	 */
	private List<String> receiver;

	private MessageType messageType = MessageType.SINGLE;

	/**
	 * Text der Nachricht.
	 */
	private String text;

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

	public String toString() {
		return "MessageType:" + getMessageType() + ", Sender: " + sender
				+ ", Receiver: " + receiver + ", Text: " + text;
	}

	public List<String> getReceiver() {
		return receiver;
	}

	public void setReceiver(List<String> receiver) {
		this.receiver = receiver;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

}
