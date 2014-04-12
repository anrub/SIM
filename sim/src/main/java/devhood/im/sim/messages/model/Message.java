package devhood.im.sim.messages.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;


/**
 * Eine Message.
 * 
 * @author flo
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = BroadcastMessage.class),
		@Type(value = SingleMessage.class),
		@Type(value = UserStatusMessage.class),
		@Type(value = FileSendAcceptMessage.class),
		@Type(value = FileSendRejectMessage.class),
		@Type(value = FileSendRequestMessage.class) })
abstract public class Message implements Serializable {
	/**
	 * Sender Name.
	 */
	private String sender;

	/**
	 * Receiver Name.
	 */
	private List<String> receiver = new ArrayList<String>();

	/**
	 * Text der Nachricht.
	 */
	private String text;

	/**
	 * Flag gibt an, ob die Message sicher empfangen werden muss, oder ob ein
	 * Verlust in Ordnung ist. Default: true, Empfang notwendig.
	 */
	private boolean reliable = true;

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

	@Override
	public String toString() {
		return ", Sender: " + sender + ", Receiver: " + receiver + ", Text: "
				+ text;
	}

	public List<String> getReceiver() {
		return receiver;
	}

	public void setReceiver(List<String> receiver) {
		this.receiver = receiver;
	}

	public boolean isReliable() {
		return reliable;
	}

	public void setReliable(boolean reliable) {
		this.reliable = reliable;
	}

}
