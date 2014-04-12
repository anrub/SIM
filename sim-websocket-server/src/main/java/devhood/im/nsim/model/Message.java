package devhood.im.nsim.model;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import devhood.im.sim.messages.model.BroadcastMessage;

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

})
public class Message {

	private long datetime;

	private String sender;

	private String receiver;

	private String text;

	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
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
		return "Message [datetime=" + datetime + ", sender=" + sender
				+ ", receiver=" + receiver + ", text=" + text + "]";
	}

}