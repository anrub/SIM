package devhood.im.sim.service.interfaces;

import devhood.im.sim.messages.Message;
import devhood.im.sim.messages.RoomMessage;
import devhood.im.sim.model.User;

/**
 * MessageSender versendet messages.
 *
 * @author flo
 *
 */
public interface TextMessageSender {
	/**
	 * Sendet die Nachricht an den User.
	 *
	 * @param user
	 *            User.
	 * @param message
	 *            Message.
	 */
	public void sendMessage(User user, Message message);

	/**
	 * Versendet die Nachricht an alle User im System.
	 *
	 * @param message
	 *            Message.
	 */
	public void sendMessageToAllUsers(Message message);

	/**
	 * Setzt den Callback für den Empfang von messages.
	 *
	 * @param messageCallback
	 *            callback
	 */
	public void setMessageCallback(MessageCallback messageCallback);


	public void sendMessageToRoom(RoomMessage m);
}
