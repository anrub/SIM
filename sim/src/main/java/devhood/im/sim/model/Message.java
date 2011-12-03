package devhood.im.sim.model;

/**
 * Eine Message.
 * 
 * @author flo
 * 
 */
public class Message {

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
		return "Sender: " + sender + ", Receiver: " + receiver + ", Text: "
				+ text;
	}

}
