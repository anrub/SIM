package devhood.im.sim.service.interfaces;

import java.io.File;

import devhood.im.sim.messages.Message;
import devhood.im.sim.messages.RoomMessage;
import devhood.im.sim.model.User;

/**
 * MessageSender versendet messages.
 *
 * @author flo
 *
 */
public interface MessageSender {
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

	/**
	 * Sendet eine Anfrage zum Dateiversand.
	 *
	 * @param file
	 *            Datei zum versenden.
	 * @param toUser
	 *            Empfaenger.
	 * @return id der uebertragung
	 */
	public String sendFileRequest(File file, String toUser);

	/**
	 * Akzeptiert einen Dateiversand.
	 *
	 * @param username
	 *            Username
	 * @param id
	 *            id
	 * @param storeInPath
	 *            Verzeichnis in dem die Datei abgelegt wird.
	 */
	public void acceptFileMessage(String username, String id, String storeInPath);

	/**
	 * Lehnt den Dateiversand ab.
	 *
	 * @param id
	 *            Id.
	 * @param username
	 *            username.
	 */
	public void rejectFileMessage(String id, String username);

	/**
	 * Gibt den aktuellen Zustand des Dateiversands zurueck.
	 * (Gesendete/Empfangene Bytes)
	 *
	 * @param id
	 *            id
	 * @return zustand.
	 */
	public long getProgress(String id);

	public void sendMessageToRoom(RoomMessage m);
}
