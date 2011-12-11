package devhood.im.sim.event;

import devhood.im.sim.model.FileSendAcceptMessage;

/**
 * Events in der Applikation.
 * 
 * @author flo
 * 
 */
public enum Events {
	/**
	 * Ein User wurde ausgewaehlt. Parameter: String username
	 */
	USER_SELECTED,
	/**
	 * Eine neue Nachricht wurde empfangen. Parameter: Message message
	 */
	MESSAGE_RECEIVED,

	/**
	 * Zeigt das Fenster an. Keine Parameter
	 */
	SHOW_FRAME,

	/**
	 * Zeigt das tabbed pane, braucht parameter: String name.
	 */
	SHOW_MSG_TABBED_PANE,

	/**
	 * Es gibt ungelesene Nachrichten parameter: Liste von Usern, deren
	 * nachrichten noch nicht gelesen wurden.
	 */
	UNREAD_MESSAGES,

	/**
	 * Event wird beim logoff geworfen parameter: String username.
	 */
	LOGOUT,

	/**
	 * Event, wird geworfenn wenn User offline gehen und aus der Liste
	 * verschwinden parameter: String userNotice die angezeigt werden soll.
	 */
	USER_OFFLINE_NOTICE,
	/**
	 * Event, wird geworfenn wenn User online gehen und neu ins Userpanel kommen
	 * parameter: String userNotice die angezeigt werden soll.
	 */
	USER_ONLINE_NOTICE,
	/**
	 * Wird geworfen, wen der Server initialisiert wurde. Parameter: null
	 */
	SERVER_INITIALISED,
	/**
	 * wird geworfen wenn ein Fehler beim senden auftrat parameter: Exception e
	 */
	MESSAGE_SEND_FAILED,
	/**
	 * Anfrage zur Dateiubertragung empfangen. Parameter :
	 * FileSendRequestMessage m
	 */
	MESSAGE_FILE_REQUEST_RECEIVED,
	/**
	 * Akzeptieren des dateiempfangs empfangen. Parameter :
	 * {@link FileSendAcceptMessage} m.
	 */
	MESSAGE_FILE_ACCEPT_RECEIVED,
	/**
	 * Ein Dateiversand wurde abgelehnt.
	 */
	MESSAGE_FILE_REJECT_RECEIVED;
}
