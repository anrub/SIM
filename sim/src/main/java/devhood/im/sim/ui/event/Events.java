package devhood.im.sim.ui.event;

/**
 * Events in der ui.
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
	SHOW_MSG_TABBED_PANE;

}
