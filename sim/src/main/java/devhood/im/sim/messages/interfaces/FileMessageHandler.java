package devhood.im.sim.messages.interfaces;

import java.io.File;

import devhood.im.sim.messages.MessagingException;

public interface FileMessageHandler {

	/**
	 * Sendet eine Anfrage zum Dateiversand.
	 *
	 * @param file
	 *            Datei zum versenden.
	 * @param toUser
	 *            Empfaenger.
	 * @return id der uebertragung
	 */
	public String sendFileRequest(File file, String toUser)
			throws MessagingException;

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
	public void acceptFileMessage(String username, String id, String storeInPath)
			throws MessagingException;

	/**
	 * Lehnt den Dateiversand ab.
	 *
	 * @param id
	 *            Id.
	 * @param username
	 *            username.
	 */
	public void rejectFileMessage(String id, String username)
			throws MessagingException;

	/**
	 * Gibt den aktuellen Zustand des Dateiversands zurueck.
	 * (Gesendete/Empfangene Bytes)
	 *
	 * @param id
	 *            id
	 * @return zustand.
	 */
	public long getProgress(String id);
}
