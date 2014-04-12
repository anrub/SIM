package devhood.im.nsim.model;

import java.util.List;

public class GetContentResponse extends Message {
	private String id;

	private List<Message> messages;

	@Override
	public String getText() {
		return "Id: " + id + "Messages: " + messages.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
