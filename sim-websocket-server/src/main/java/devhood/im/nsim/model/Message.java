package devhood.im.nsim.model;

import java.util.UUID;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import devhood.im.sim.messages.model.BroadcastMessage;

/**
 * Eine Message, die zwischen UI und core ausgetauscht wird.
 * 
 * @author flo
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = BroadcastMessage.class),
		@Type(value = SendMessage.class), @Type(value = NewMessage.class),
		@Type(value = UpdateStatus.class), @Type(value = FileSendAccept.class),
		@Type(value = FileSendReject.class),
		@Type(value = FileSendProgress.class),
		@Type(value = FileSendRequest.class), @Type(value = GetUserlist.class),
		@Type(value = GetUserlistResponse.class),
		@Type(value = UserUpdateNotice.class),
		@Type(value = UserOnlineNotice.class),
		@Type(value = UserOfflineNotice.class),
		@Type(value = UnreadMessages.class), @Type(value = OpenRoom.class),
		@Type(value = GetContent.class),
		@Type(value = GetContentResponse.class),
		@Type(value = GetRoomlist.class),
		@Type(value = GetRoomlistResponse.class),
		@Type(value = JoinRoom.class), @Type(value = RoomEnterNotice.class),
		@Type(value = RoomLeaveNotice.class), @Type(value = RoomOpened.class),
		@Type(value = ErrorMessage.class) })
public class Message {

	/**
	 * Sender, falls leer wird von core gefuellt beim Versand.
	 */
	private String sender;

	/**
	 * Empfaenger Bezeichner.
	 */
	private String receiver;

	/**
	 * Text-Inhalt.
	 */
	private String text;

	/**
	 * Eindeutige ID dieser Message.
	 */
	private String id = UUID.randomUUID().toString();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Message [ sender=" + sender + ", receiver=" + receiver
				+ ", text=" + text + "]";
	}

}