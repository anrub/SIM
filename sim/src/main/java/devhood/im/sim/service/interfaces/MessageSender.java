package devhood.im.sim.service.interfaces;

import devhood.im.sim.model.Message;
import devhood.im.sim.model.User;

public interface MessageSender {
	public void sendMessage(User user, Message message);

	public void sendMessageToAllUsers(Message message);

	public void setMessageCallback(MessageCallback messageCallback);
}
