package devhood.im.nsim.model;

import java.util.List;

public class GetContentResponse extends Message {
	private String conversation;

	private List<Message> messages;

	@Override
	public String getText() {
		return "conversation: " + conversation + "Messages: "
				+ messages.toString();
	}

	public List<Message> getMessages() {
		return messages;
	}

	public String getConversation() {
		return conversation;
	}

	public void setConversation(String conversation) {
		this.conversation = conversation;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
}
