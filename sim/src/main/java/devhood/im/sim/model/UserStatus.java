package devhood.im.sim.model;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Status eines Benutzers.
 *
 * @author flo
 *
 */
public enum UserStatus implements Serializable {

	/**
	 * Benutzer ist verfuegbar.
	 */
	AVAILABLE("verf\u00FCgbar"),
	/**
	 * Benutzer ist nicht verfuegbar - aber trotzdem weiter anschreibbar.
	 */
	NOT_AVAILABLE("nicht verf\u00FCgbar"),
	/**
	 * Benutzer ist prinzipiell da, aber beschaeftigt.
	 */
	BUSY("besch\u00E4ftigt");

	/**
	 * Um den reverse lookup zu gewaehrleisten, also String -> enum.
	 */
	private static final Map<String, UserStatus> lookup = new HashMap<String, UserStatus>();

	static {
		for (UserStatus s : EnumSet.allOf(UserStatus.class))
			lookup.put(s.getText(), s);
	}

	private String text;

	private UserStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public static UserStatus get(String text) {
		return lookup.get(text);
	}

	@Override
	public String toString() {
		return text;
	}
}