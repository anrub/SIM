package devhood.im.sim.ui.event;

import devhood.im.sim.messages.model.FileSendAcceptMessage;

/**
 * Events in der Applikation.
 *
 * @author flo
 *
 */
public enum Events {
	SHOW_CONFIGURATION_FRAME,
	/**
	 * Ein User wurde ausgewaehlt. Parameter: String username
	 */
	RECEIVER_SELECTED,
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
