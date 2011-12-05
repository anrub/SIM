package devhood.im.sim.event;

/**
 * Events in der Applikation.
 * 
 * @author flo
 * 
 */
public enum Events {
	/**
	 * Ein User wurde ausgewaehlt. Parameter JCheckBox checkbox
	 */
	USER_SELECTED,

	/**
	 * Eine neue Nachricht wurde gesendet. Parameter: Message message
	 */
	MESSAGE_SENT,
	
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
	USER_OFFLINE_NOTICE;

}
